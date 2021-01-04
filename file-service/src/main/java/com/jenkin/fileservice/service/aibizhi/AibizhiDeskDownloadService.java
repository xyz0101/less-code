package com.jenkin.fileservice.service.aibizhi;

import com.jenkin.common.config.FeignRequestConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jenkin
 * @className AibizhiDeskDownloadService
 * @description TODO
 * @date 2020/12/29 10:39
 */
@FeignClient(name = "aibizhiService",url = "${third.aibizhi.desk-download}",configuration = FeignRequestConfig.class)
public interface AibizhiDeskDownloadService {

    @GetMapping("/{imgId}")
    ResponseEntity<byte[]> downloadFile(@RequestParam("imgId") String imgId);

}
