package com.jenkin.systemservice.history.service;

import com.jenkin.common.config.FeignRequestConfig;
import com.jenkin.common.entity.Response;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author jenkin
 * @className OAuthService
 * @description TODO
 * @date 2021/2/1 16:22
 */
@FeignClient(name = "oauthService",url = "${third.ssxx-oauth.url}",configuration = FeignRequestConfig.class)
public interface OAuthService {
    @GetMapping("/5f582dd3683c2e0ae3aaacee")
    Response<QrCode> getQrCode(@RequestParam("random") String random, @RequestParam("useSelfWxapp") Boolean useSelfWxapp,
                       @RequestParam("enableFetchPhone") Boolean enableFetchPhone);

    @Data
    static class QrCode{
        private String random;
        private Boolean used;
        private String accessToken;
        private String openid;
        private Boolean success;
        private String userInfo;
        private String redirect;
        private String _id;
        private String client;
        private String oauth;
        private String oauthWithApplication;
        private String qrcode;
        private Date expiredAt;
        private Date createdAt;
        private Integer __v;
    }
}
