package com.jenkin.sometools.btsearch.controller;

import com.jenkin.common.entity.Response;
import com.jenkin.sometools.btsearch.vo.BtInfoVO;
import io.swagger.annotations.Api;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    @GetMapping("/listAll")
    public Response<List<BtInfoVO>> findAll(String id) {
        Query all = Query.findAll();
//        NativeSearchQuery query = new NativeSearchQuery();
        SearchHits<BtInfoVO> search = elasticsearchOperations
                .search(all, BtInfoVO.class);
        long totalHits = search.getTotalHits();
        List<BtInfoVO> collect = search.get().map(item -> item.getContent()).collect(Collectors.toList());
        return Response.ok(collect);
    }




}
