package com.jenkin.fileservice.controller.aibizhi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jenkin.common.anno.IgnoreCheck;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.dtos.file.WallpaperConfigDto;
import com.jenkin.common.entity.vos.aibizhi.AbzResponse;
import com.jenkin.common.entity.vos.aibizhi.Category;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import com.jenkin.common.entity.vos.aibizhi.WallpaperConfigVO;
import com.jenkin.common.enums.TimeUnitEnum;
import com.jenkin.common.shiro.utils.ShiroUtils;
import com.jenkin.common.utils.ApplicationContextProvider;
import com.jenkin.common.utils.FileUtils;
import com.jenkin.fileservice.service.aibizhi.AibizhiDeskDownloadService;
import com.jenkin.fileservice.service.aibizhi.AibizhiService;
import com.jenkin.fileservice.service.aibizhi.WallpaperConfigService;
import com.jenkin.fileservice.wallpaper_server.strategy.BaseStrategy;
import com.jenkin.fileservice.wallpaper_server.strategy.impl.RandomStrategy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

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
    private WallpaperConfigService wallpaperConfigService;

    @Autowired
    AibizhiService aibizhiService;
    @Autowired
    AibizhiDeskDownloadService deskDownloadService;
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

    @GetMapping("/getAbzWallpaperWin")
    @ApiOperation("根据分类获取爱壁纸的壁纸信息(非网页版)")
    public Response<AbzResponse<Wallpaper>> getAbzWallpaperWin(String category,Integer skip){

        return Response.ok(aibizhiService.getWallpaper(category,skip));
    }


    @GetMapping("/downloadWallpaper")
    @ApiOperation("下载壁纸(非网页版)")
    public void downloadWallpaper(@RequestParam("imgId") String imgId, HttpServletResponse response){
        ResponseEntity<byte[]> responseEntity = deskDownloadService.downloadFile(imgId);
        byte[] body = responseEntity.getBody();
        List<String> contentType = responseEntity.getHeaders().get("Content-Type");
        String type = contentType.get(0);
        response.setHeader("Content-Type",type);
        String name = "img." + type.substring(6 );
        FileUtils.downloadFile(name,new ByteArrayInputStream(body),response);
    }

    @PostMapping("/saveConfig")
    @ApiOperation("保存壁纸配置信息")
    public Response<String> saveConfig(@RequestBody WallpaperConfigVO<JSONObject> configVO){
        WallpaperConfigDto configByUser = wallpaperConfigService.getConfigByUser(ShiroUtils.getUserCode());
        configByUser.setUserCode(ShiroUtils.getUserCode());
        configByUser.setOnFlag(configVO.getOn());
        configVO.getData().put("userCode",ShiroUtils.getUserCode());
        configByUser.setStrategyValue(JSON.toJSONString(configVO.getData()));

        wallpaperConfigService.saveWallpaperConfigInfo(configByUser);
        return Response.ok();
    }

    @GetMapping("/getConfig")
    @ApiOperation("获取壁纸配置信息")
    @IgnoreCheck
    public Response<WallpaperConfigVO<JSONObject>> getConfig(){

        WallpaperConfigDto configByUser = wallpaperConfigService.getConfigByUser(ShiroUtils.getUserCode());
        if(configByUser==null){
            return Response.ok();
        }
        WallpaperConfigVO<JSONObject> configVO = new WallpaperConfigVO<>();
        configVO.setOn(configByUser.getOnFlag());
        configVO.setData( JSON.parseObject(configByUser.getStrategyValue()));
        return Response.ok(configVO);
    }

    @PostMapping("/changeWallpaper")
    @ApiOperation("修改系统桌面壁纸")
    public Response changeWallpaper(@RequestBody Wallpaper wallpaper){
        wallpaperConfigService.changeWallpaper(wallpaper);
        return Response.ok();
    }

    @GetMapping("/getStrategy")
    @ApiOperation("获取壁纸策略")
    public Response<List<BaseStrategy>> getStrategy(){
        String[] beanNamesForType = ApplicationContextProvider.getApplicationContext().getBeanNamesForType(BaseStrategy.class);
        List<BaseStrategy> baseStrategies = new ArrayList<>();
        for (String s : beanNamesForType) {
            baseStrategies.add(ApplicationContextProvider.getBean(s,BaseStrategy.class));
        }
        return Response.ok(baseStrategies);
    }



}
