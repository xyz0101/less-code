//package com.jenkin.common.utils.demo.essync;
//
//import cn.hutool.db.*;
//import cn.hutool.db.ds.DSFactory;
//import cn.hutool.db.ds.DataSourceWrapper;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.xnx3.elasticsearch.ElasticSearchUtil;
//import org.elasticsearch.index.query.QueryBuilders;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.PriorityQueue;
//
///**
// * @author ：jenkin
// * @date ：Created at 2021/3/15 20:24
// * @description：
// * @modified By：
// * @version: 1.0
// */
//public class SyncToEs {
//
//    public static void main(String[] args) throws SQLException {
//
//        ElasticSearchUtil elasticSearchUtil = new ElasticSearchUtil("192.168.3.71");
//        int i=0;
//        while(true){
//            PageResult<Entity> result = Db.use()
//                .page(Entity.create("weibo"), new Page(i, 5000));
//            if(result.isEmpty()){
//                break;
//            }
//            for (Entity entity : result) {
//                Weibo weibo = entity.toBean(Weibo.class);
//                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(weibo));
////                List<Map<String, Object>> w =
////                        elasticSearchUtil.search("weibo", "id:"+weibo.getId());
//                elasticSearchUtil.put(jsonObject,"weibo",weibo.getId());
//            }
//             i++;
//        }
//
//
//
//
//
//
//
//
//    }
//
//}
