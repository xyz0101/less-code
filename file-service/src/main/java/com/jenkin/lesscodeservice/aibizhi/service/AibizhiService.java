package com.jenkin.lesscodeservice.aibizhi.service;

import com.jenkin.common.config.FeignRequestConfig;
import com.jenkin.common.entity.vos.aibizhi.AbzResponse;
import com.jenkin.common.entity.vos.aibizhi.Category;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jenkin
 * @className AibizhiService
 * @description TODO
 * @date 2020/12/28 16:22
 */
@FeignClient(name = "aibizhiService",url = "${third.aibizhi.url}",configuration = FeignRequestConfig.class)
public interface AibizhiService {
    @GetMapping("/category")
    AbzResponse<Category> getAibizhiCategory();

    @GetMapping("/category/{typeCode}/wallpaper")
    @Headers("{'User-Agent':picasso,170,windows}")
    AbzResponse<Wallpaper> getWallpaper(@RequestParam("typeCode") String typeCode,@RequestParam("skip") Integer skip);


}
