package com.jenkin.sometools.pic_background.controller;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.jenkin.common.anno.EnableErrorCatch;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.vos.pic_deal.PicDealRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import static com.jenkin.sometools.util.PicUtils.*;

/**
 * @author jenkin
 * @className PicDealController
 * @description TODO
 * @date 2021/3/11 12:04
 */
@RestController
@RequestMapping("/picDeal")
@Api(tags = "ai图片处理百度开放接口")
@CrossOrigin
@EnableErrorCatch("picDeal")
public class PicDealController {

    @PostMapping("/removeBackground")
    @ApiOperation("人像抠图，去除背景")
    public Response<PicDealRes> removeBackground(@RequestParam("file") MultipartFile file){

        // 初始化一个AipBodyAnalysis
        AipBodyAnalysis client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
//        options.put("type", "foreground");
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
        // 调用接口
        byte[] bytes = new byte[0];
        try {
            bytes = IOUtils.toByteArray(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject res = client.bodySeg(bytes ,options);
        String jsonRes = res.toString();
        PicDealRes picDealRes = JSON.parseObject(jsonRes, PicDealRes.class);
        return Response.ok(picDealRes);


    }



}
