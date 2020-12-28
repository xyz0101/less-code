package com.jenkin.lesscodeservice.aibizhi.controller;

import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.vos.aibizhi.AbzResponse;
import com.jenkin.common.entity.vos.aibizhi.Category;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import com.jenkin.lesscodeservice.aibizhi.service.AibizhiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author jenkin
 * @className AiBizhiController
 * @description TODO
 * @date 2020/12/28 16:20
 */
@RestController
@CrossOrigin
@RequestMapping("/aibizhi")
@Api(tags = "获取爱壁纸")
public class AiBizhiController {
    @Autowired
    AibizhiService aibizhiService;

    @GetMapping("/getAbzCategory")
    @ApiOperation("获取爱壁纸的分类信息")
    public Response<AbzResponse<Category>> getAbzCategory(){
        return Response.ok(aibizhiService.getAibizhiCategory());
    }
    @GetMapping("/getAbzWallpaper")
    @ApiOperation("根据分类获取爱壁纸的壁纸信息")
    public Response<AbzResponse<Wallpaper>> getAbzWallpaper(String category,Integer skip){

        return Response.ok(aibizhiService.getWallpaper(category,skip));
    }
}
