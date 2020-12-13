package com.jenkin.common.shiro.controller;

import com.jenkin.common.anno.IgnoreCheck;
import com.jenkin.common.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/12 13:45
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping("/shiro")
@CrossOrigin
@Slf4j
public class ShiroController {
    @GetMapping("/needLogin")
    @IgnoreCheck
    public Response needLogin(){
        log.error("请登录");
        return Response.error("请登录");
    }


}
