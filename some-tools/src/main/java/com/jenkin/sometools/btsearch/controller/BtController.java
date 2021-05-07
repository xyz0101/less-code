package com.jenkin.sometools.btsearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.entity.Response;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.sometools.btsearch.service.BtService;
import com.jenkin.sometools.btsearch.service.SyncElasticSearchService;
import com.jenkin.sometools.btsearch.vo.BtInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/25 21:25
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping("/bt")
@Api(tags = "bt搜索")
@CrossOrigin
public class BtController {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;
    @Autowired
    private SyncElasticSearchService syncElasticSearchService;


    @PostMapping("/save")
    public Response<String> save(@RequestBody BtInfoVO btInfoVO) {

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(btInfoVO.getInfohash())
                .withObject(btInfoVO)
                .build();
        String documentId = elasticsearchOperations.index(indexQuery,IndexCoordinates.of("bt_info"));
        return Response.ok(documentId);
    }

    @GetMapping("/getInfo")
    public Response<BtInfoVO> findById(String id) {
        BtInfoVO btInfoVO = elasticsearchOperations
                .get(id, BtInfoVO.class);
        return Response.ok(btInfoVO);
    }

    @GetMapping("/listPage")
    @ApiOperation("分页查询")
    public Response<Page<BtInfoVO>> listPage(String keyword,Integer pageSize,Integer page){

//        Sort sort = Sort.by(Sort.Order.desc("length"));
        Pageable pageable = PageRequest.of(page,pageSize);
        NativeSearchQueryBuilder  builder = new NativeSearchQueryBuilder ();
         SearchHits<BtInfoVO> search =null;
        if (StringUtils.hasLength(keyword)){
            builder.withPageable(pageable).withQuery(QueryBuilders.queryStringQuery(keyword) );
            NativeSearchQuery build = builder.build();
            search = elasticsearchOperations.search(build, BtInfoVO.class);
        }else{
            Query all = Query.findAll();
            all.setPageable(pageable);
            search=elasticsearchOperations.search(all, BtInfoVO.class);
        }



        long totalHits = search.getTotalHits();

        List<BtInfoVO> collect = search.get().map(item -> {
            BtInfoVO content = item.getContent();
            content.setScore(item.getScore());
            return content;
            }
         ).collect(Collectors.toList());
        Page<BtInfoVO> res = new Page<>();
        res.setRecords(collect);
        res.setTotal(totalHits);
        res.setSize(pageSize);
        return Response.ok(res);
    }

    @GetMapping("/syncAll")
    @ApiOperation("全量同步")
    public Response syncAll(Integer beforeDay){
        syncElasticSearchService.syncAll(beforeDay);
        return Response.ok();
    }



}
