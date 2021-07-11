package com.jenkin.sometools.btsearch.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.constant.DataSourceConst;
import com.jenkin.common.constant.ThreadPoolConst;
import com.jenkin.common.entity.pos.bt.BtInfoPO;
import com.jenkin.common.utils.DateUtils;
import com.jenkin.common.utils.Redis;
import com.jenkin.sometools.btsearch.dao.BtMapper;
import com.jenkin.sometools.btsearch.vo.BtInfoVO;
import com.jenkin.sometools.btsearch.vo.LengthVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/26 20:15
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Service
@Slf4j
public class SyncElasticSearchService {
    @Resource
    private BtMapper btMapper;
    private static final String LAST_SYNC_DATE="sync:es:last:date";
    private static final String LAST_SYNC_ID="sync:es:last:id";
    @Autowired
    private BtService btService;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private Redis redis;
//    @Scheduled(cron = "0 0 0/1 * * ?")
    public void syncToEs(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       log.info("每小时同步数据到es");
        Object o = redis.get(LAST_SYNC_DATE);

        try {
            Date startDate = o==null?new Date():simpleDateFormat.parse(o.toString());
            Date endDate = DateUtil.offsetHour(startDate,1);
            redis.set(LAST_SYNC_DATE,simpleDateFormat.format(endDate));
            syncByDate(startDate,endDate,1);


        } catch (ParseException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void syncById(long start,long end ,int offset) throws InterruptedException {
        long theend = end;
        log.info("整体需要同步的id区间：{}---{}",start,end);
        //每次偏移n个小时
        while(start<=theend) {
           end = start+offset;
            log.info("分段需要同步的id区间：{}---{}",start,end);
            MyQueryWrapper<BtInfoPO> query = new MyQueryWrapper<>();
            query.between("id", start, end);
            List<BtInfoPO> list = btService.list(query);
            start = end;
            syncList(list);
            redis.set(LAST_SYNC_ID,start);

        }
    }

    private void syncList(List<BtInfoPO> list) throws InterruptedException {
        log.info("需要插入{}条数据", list.size());
        final CountDownLatch countDownLatch = new CountDownLatch(list.size());

        for (BtInfoPO btInfoPO : list) {
            ThreadPoolConst.EXAM_TASK_JOBS_EXECUTORS.execute(() -> {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BtInfoVO btInfoVO = JSON.parseObject(btInfoPO.getBtData(), BtInfoVO.class);
                try {
                    btInfoVO.setRecordTime(sdf.parse(DateUtils.parseTime(btInfoPO.getCreationDate())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long total = 0;
                if (btInfoVO.getLength() == 0) {
                    if (btInfoVO.getFiles() != null) {
                        for (BtInfoVO.FileInfo file : btInfoVO.getFiles()) {
                            setLength(file);
                            total += file.getLength();
                        }
                    }
                    btInfoVO.setLength(total);
                }
                if (btInfoVO.getLength() > 0) {
                    setLength(btInfoVO);
                }
                IndexQuery indexQuery = new IndexQueryBuilder()
                        .withId(btInfoVO.getInfohash())
                        .withObject(btInfoVO)
                        .build();
                elasticsearchOperations.index(indexQuery, IndexCoordinates.of("bt_info"));
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }


    /**
     *
     * @param startDate
     * @param endDate
     * @param offset 每次往后偏移多少个小时
     */
    private void syncByDate(Date startDate, Date endDate,int offset) throws InterruptedException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date theEnd = endDate;
        log.info("整体需要同步的开始结束区间：{}---{}",format.format(startDate),format.format(endDate));
        //每次偏移n个小时
        while(DateUtil.compare(startDate,theEnd)<=0) {
            endDate = DateUtil.offsetHour(startDate,offset);
            log.info("分段需要同步的开始结束区间：{}---{}",format.format(startDate),format.format(endDate));
            MyQueryWrapper<BtInfoPO> query = new MyQueryWrapper<>();
            query.between("creation_data", startDate, endDate);
            List<BtInfoPO> list = btService.list(query);
            startDate = endDate;
            syncList(list);


        }
    }

    private void setLength(LengthVO lengthVO) {
        lengthVO.setTotalLengthKb(new BigDecimal((double) lengthVO.getLength()/1024).setScale(3,
                BigDecimal.ROUND_HALF_UP).doubleValue());
        lengthVO.setTotalLengthMb(new BigDecimal((double) lengthVO.getLength()/(1024*1024)).setScale(3,
                BigDecimal.ROUND_HALF_UP).doubleValue());
        lengthVO.setTotalLengthGb(new BigDecimal((double) lengthVO.getLength()/(1024*1024*1024)).setScale(3,
                BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     * 全量同步
     */
    public void syncAll(int beforeDay){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long start = 0;
        Object o = redis.get(LAST_SYNC_ID);
        if (o!=null){
            start = Long.parseLong(String.valueOf(o));
        }
       long end =btMapper.getMaxId();
        try {
            syncById(start,end,1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redis.set(LAST_SYNC_ID,end);
        //更新最新的结束时间
        redis.set(LAST_SYNC_DATE,simpleDateFormat.format(new Date()));

    }



}
