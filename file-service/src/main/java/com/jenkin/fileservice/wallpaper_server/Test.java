package com.jenkin.fileservice.wallpaper_server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.vos.aibizhi.AbzResponse;
import com.jenkin.common.entity.vos.aibizhi.Category;
import com.jenkin.fileservice.wallpaper_server.strategy.WallpaperStrategy;
import com.jenkin.fileservice.wallpaper_server.strategy.impl.OrderStrategy;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2021/9/26 12:48
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(5 * 1024 * 1024 * 1024);
        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(5 * 1024 * 1024 * 1024);
        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(5 * 1024 * 1024 * 1024);
        ByteBuffer byteBuffer3 = ByteBuffer.allocateDirect(5 * 1024 * 1024 * 1024);
        ByteBuffer byteBuffer4 = ByteBuffer.allocateDirect(5 * 1024 * 1024 * 1024);
        ByteBuffer byteBuffer5 = ByteBuffer.allocateDirect(5 * 1024 * 1024 * 1024);
        ByteBuffer byteBuffer6 = ByteBuffer.allocateDirect(5 * 1024 * 1024 * 1024);
//        ByteBuffer byteBuffer7 = ByteBuffer.allocateDirect(1 * 1024 * 1024 * 128);
//        ByteBuffer byteBuffer8 = ByteBuffer.allocateDirect(5 * 1024 * 1024 * 1024);
//        ByteBuffer byteBuffer9 = ByteBuffer.allocateDirect(5 * 1024 * 1024 * 1024);
        Thread.sleep(60000);

        String json = "{\"code\":\"200\",\"msg\":\"请求成功\",\"data\":{\"msg\":\"success\",\"res\":{\"wallpaper\":null,\"category\":[{\"count\":50741,\"ename\":\"girl\",\"rname\":\"美女\",\"cover_temp\":\"56a964df69401b2866828acb\",\"name\":\"美女\",\"cover\":\"http://img.aibizhi.adesk.com/614c5943e7bce72b931d420f?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=1f7a78b8c79de3a07406a9ed3ab56fa2&t=61537da4\",\"rank\":1,\"filter\":[],\"sn\":1,\"icover\":\"582c34f869401b347e0b43fb\",\"atime\":1291266021,\"type\":1,\"id\":\"4e4d610cdf714d2966000000\",\"picasso_cover\":\"614c5943e7bce72b931d420f\"},{\"count\":93572,\"ename\":\"animation\",\"rname\":\"动漫\",\"cover_temp\":\"56a221c969401b3f4aa6700a\",\"name\":\"动漫\",\"cover\":\"http://img.aibizhi.adesk.com/6144325ce7bce72af5e518a4?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=a6a50b22e24f7c3a608a6cbf3cdf60ef&t=61537da4\",\"rank\":4,\"filter\":[],\"sn\":2,\"icover\":\"5880889ae7bce7755f3607d9\",\"atime\":1291266057,\"type\":1,\"id\":\"4e4d610cdf714d2966000003\",\"picasso_cover\":\"6144325ce7bce72af5e518a4\"},{\"count\":72666,\"ename\":\"landscape\",\"rname\":\"风景\",\"cover_temp\":\"56a770e269401b756c748b28\",\"name\":\"风景\",\"cover\":\"http://img.aibizhi.adesk.com/614b0f73e7bce72af5e51b64?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=f4c89d086ff62e62ea9a8112b3ba3a55&t=61537da4\",\"rank\":3,\"filter\":[],\"sn\":3,\"icover\":\"581b0f2a69401b34865e6cd2\",\"atime\":1291266049,\"type\":1,\"id\":\"4e4d610cdf714d2966000002\",\"picasso_cover\":\"614b0f73e7bce72af5e51b64\"},{\"count\":14459,\"ename\":\"game\",\"rname\":\"游戏\",\"cover_temp\":\"569f40fa69401b26c648eb87\",\"name\":\"游戏\",\"cover\":\"http://img.aibizhi.adesk.com/612cc43fe7bce775ffcae7d4?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=337ff157c848ee5f09078efd3acf405d&t=61537da4\",\"rank\":15,\"filter\":[],\"sn\":4,\"icover\":\"5866127069401b347e0bd82b\",\"atime\":1300683934,\"type\":1,\"id\":\"4e4d610cdf714d2966000007\",\"picasso_cover\":\"612cc43fe7bce775ffcae7d4\"},{\"count\":9644,\"ename\":\"text\",\"rname\":\"文字\",\"cover_temp\":\"56a1f92369401b3f529d3a3f\",\"name\":\"文字\",\"cover\":\"http://img.aibizhi.adesk.com/6140068fe7bce7764177bfcd?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=454a4c70a3f77a3500e793a6442e9b3c&t=61537da4\",\"rank\":5,\"filter\":[],\"sn\":5,\"icover\":\"5864e5a769401b34865f1ccc\",\"atime\":1359601742,\"type\":1,\"id\":\"5109e04e48d5b9364ae9ac45\",\"picasso_cover\":\"6140068fe7bce7764177bfcd\"},{\"count\":8134,\"ename\":\"vision\",\"rname\":\"视觉\",\"cover_temp\":\"56a076f769401b323d865538\",\"name\":\"视觉\",\"cover\":\"http://img.aibizhi.adesk.com/61447ad6e7bce72b0aa8840f?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=e341686be349db4ec22f720e8bc716a6&t=61537da4\",\"rank\":8,\"filter\":[],\"sn\":6,\"icover\":\"57f8be3d69401b347e0ab423\",\"atime\":0,\"type\":1,\"id\":\"4fb479f75ba1c65561000027\",\"picasso_cover\":\"61447ad6e7bce72b0aa8840f\"},{\"count\":15103,\"ename\":\"emotion\",\"rname\":\"情感\",\"cover_temp\":\"56a03f5369401b26beeaea1d\",\"name\":\"情感\",\"cover\":\"http://img.aibizhi.adesk.com/614043dce7bce775ffcaede2?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=46d1157aab23a8e58d8111ebe8b5ffdf&t=61537da4\",\"rank\":2,\"filter\":[],\"sn\":7,\"icover\":\"57c53c8769401b644d2782fb\",\"atime\":0,\"type\":1,\"id\":\"4ef0a35c0569795756000000\",\"picasso_cover\":\"614043dce7bce775ffcaede2\"},{\"count\":8214,\"ename\":\"creative\",\"rname\":\"设计\",\"cover_temp\":\"569b34af69401b7dd39e9fc3\",\"name\":\"设计\",\"cover\":\"http://img.aibizhi.adesk.com/61443eb4e7bce72b20e35c8e?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=cd314702cd4f9d9268057dc9399db1ae&t=61537da4\",\"rank\":9,\"filter\":[],\"sn\":8,\"icover\":\"575e7a9869401b01d8ef3ece\",\"atime\":0,\"type\":1,\"id\":\"4fb47a195ba1c60ca5000222\",\"picasso_cover\":\"61443eb4e7bce72b20e35c8e\"},{\"count\":19797,\"ename\":\"celebrity\",\"rname\":\"明星\",\"cover_temp\":\"56a9a70669401b338161138c\",\"name\":\"明星\",\"cover\":\"http://img.aibizhi.adesk.com/6140636ae7bce7766e8d6649?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=0b324de8923739994b6f2a8aaf0ba955&t=61537da4\",\"rank\":6,\"filter\":[],\"sn\":9,\"icover\":\"5460349269401b3a428a47a7\",\"atime\":1359601746,\"type\":1,\"id\":\"5109e05248d5b9368bb559dc\",\"picasso_cover\":\"6140636ae7bce7766e8d6649\"},{\"count\":23969,\"ename\":\"stuff\",\"rname\":\"物语\",\"cover_temp\":\"56a61f1c69401b54eff72f31\",\"name\":\"物语\",\"cover\":\"http://img.aibizhi.adesk.com/614473a4e7bce72b513365af?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=dc7abfde43cbb7d4781cb7545bcf7e78&t=61537da4\",\"rank\":10,\"filter\":[],\"sn\":10,\"icover\":\"557b8cf269401b1704e91bfc\",\"atime\":0,\"type\":1,\"id\":\"4fb47a465ba1c65561000028\",\"picasso_cover\":\"614473a4e7bce72b513365af\"},{\"count\":4229,\"ename\":\"man\",\"rname\":\"男人\",\"cover_temp\":\"569b541d69401b7dc8ce2c68\",\"name\":\"男人\",\"cover\":\"http://img.aibizhi.adesk.com/6103f1c6e7bce71e0489ced4?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=9673925b6085f9aa08034a36928333a5&t=61537da4\",\"rank\":13,\"filter\":[],\"sn\":12,\"icover\":\"550ba05469401b46dbf0b687\",\"atime\":1298251540,\"type\":1,\"id\":\"4e4d610cdf714d2966000006\",\"picasso_cover\":\"6103f1c6e7bce71e0489ced4\"},{\"count\":23698,\"ename\":\"machine\",\"rname\":\"机械\",\"cover_temp\":\"56a99e1f69401b1ce58c12dc\",\"name\":\"机械\",\"cover\":\"http://img.aibizhi.adesk.com/60e7cb8be7bce70ef91bcb68?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=753151d4be014362ccc68c561ffa7448&t=61537da4\",\"rank\":12,\"filter\":[],\"sn\":13,\"icover\":\"5028b42aedd6a9410c002552\",\"atime\":1297756191,\"type\":1,\"id\":\"4e4d610cdf714d2966000005\",\"picasso_cover\":\"60e7cb8be7bce70ef91bcb68\"},{\"count\":13628,\"ename\":\"cityscape\",\"rname\":\"城市\",\"cover_temp\":\"569b540969401b7dd39ea06d\",\"name\":\"城市\",\"cover\":\"http://img.aibizhi.adesk.com/614476ece7bce72b7e29dc8f?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=08170d4a505e56ea91b45bacaf7359de&t=61537da4\",\"rank\":7,\"filter\":[],\"sn\":14,\"icover\":\"5792cf7369401b71e3555741\",\"atime\":0,\"type\":1,\"id\":\"4fb47a305ba1c60ca5000223\",\"picasso_cover\":\"614476ece7bce72b7e29dc8f\"},{\"count\":19477,\"ename\":\"animal\",\"rname\":\"动物\",\"cover_temp\":\"56a4d1da69401b753a684e69\",\"name\":\"动物\",\"cover\":\"http://img.aibizhi.adesk.com/6136cb07e7bce775ffcaeb2f?imageMogr2/thumbnail/!640x480r/gravity/Center/crop/640x480&sign=813bfefe907d23816e79c0c259ca81e6&t=61537da4\",\"rank\":14,\"filter\":[],\"sn\":16,\"icover\":\"58636cda69401b34865f1406\",\"atime\":1291266042,\"type\":1,\"id\":\"4e4d610cdf714d2966000001\",\"picasso_cover\":\"6136cb07e7bce775ffcaeb2f\"}]},\"code\":0}}";

        Response<AbzResponse<Category>> res = JSON.parseObject(json,new TypeReference<Response<AbzResponse<Category>>>(){});
        List<String> collect = res.getData().getRes().getCategory().stream().map(item -> item.getId()).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(collect));

    }
}
