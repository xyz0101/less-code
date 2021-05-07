package com.jenkin.sometools.btsearch.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.constant.DataSourceConst;
import com.jenkin.common.constant.ThreadPoolConst;
import com.jenkin.common.entity.pos.bt.BtInfoPO;
import com.jenkin.common.utils.DateUtils;
import com.jenkin.common.utils.Redis;
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

    private static final String LAST_SYNC_DATE="sync:es:last:date";
    @Autowired
    private BtService btService;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private Redis redis;
    @Scheduled(cron = "0 0 0/1 * * ?")
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
            log.info("需要插入{}条数据", list.size());
            final CountDownLatch countDownLatch = new CountDownLatch(list.size());
            startDate = endDate;
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
        Date startDate =DateUtil.offsetDay(new Date(),-beforeDay);
        Date endDate = new Date();
        try {
            syncByDate(startDate,endDate,5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //更新最新的结束时间
        redis.set(LAST_SYNC_DATE,simpleDateFormat.format(endDate));

    }



}
