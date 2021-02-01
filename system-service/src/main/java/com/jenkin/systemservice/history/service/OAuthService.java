package com.jenkin.systemservice.history.service;

import com.jenkin.common.config.FeignRequestConfig;
import com.jenkin.common.entity.Response;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @GetMapping("/qrcode/5f582dd3683c2e0ae3aaacee")
    Response<QrCode> getQrCode(@RequestParam("random") String random, @RequestParam("useSelfWxapp") Boolean useSelfWxapp,
                       @RequestParam("enableFetchPhone") Boolean enableFetchPhone);
    @PostMapping("/confirm/qr")
    Response<UserLoginData> comfirm(@RequestParam("random") String random, @RequestParam("useSelfWxapp") Boolean useSelfWxapp
                              );





    @Data
    static class UserLoginData{
        private String email;
        private String phone;
        private Boolean emailVerified;
        private Boolean phoneVerified;
        private String username;
        private String nickname;
        private String company;
        private String photo;
        private String browser;
        private String device;
        private String password;
        private String abookPassword;
        private String aixitiPassword;
        private String xuanshuPassword;
        private String oldSsoPassword;
        private String menhuPassword;
        private String source;
        private Integer loginsCount;
        private String registerMethod;
        private Boolean blocked;
        private Boolean isDeleted;
        private String oauth;
        private String phoneCode;
        private String name;
        private String givenName;
        private String familyName;
        private String middleName;
        private String profile;
        private String preferredUsername;
        private String website;
        private String gender;
        private String birthdate;
        private String zoneinfo;
        private String locale;
        private String address;
        private String formatted;
        private String streetAddress;
        private String locality;
        private String region;
        private String postalCode;
        private String country;
        private String updatedAt;
        private String metadata;

        private Integer sendSMSCount;
        private Integer sendSMSLimitCount;
        private String from;
        private String _id;
        private String unionid;
        private String openid;
        private String lastIP;
        private String registerInClient;
        private String salt;

        private Integer __v;
        private String token;
    }


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
//        private Date expiredAt;
//        private Date createdAt;
        private Integer __v;
    }
}
