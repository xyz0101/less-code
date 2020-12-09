package com.jenkin.simpleshiro.controller;

import com.jenkin.simpleshiro.ShiroUtils;
import com.jenkin.simpleshiro.dao.UsersRepository;
import com.jenkin.simpleshiro.entity.Response;
import com.jenkin.simpleshiro.entity.User;
import com.jenkin.simpleshiro.entity.UserDto;
import com.jenkin.simpleshiro.shiro.anno.IgnoreCheck;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:44
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping("/test")
@CrossOrigin
public class Controller {

    @Resource
    private UsersRepository usersRepository;

    @GetMapping("/login")
    @IgnoreCheck
    public Response login(String username,String password){
        Subject subject = ShiroUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
        UserDto userEntity = ShiroUtils.getUserEntityNoPermissionStr();
        return Response.ok(userEntity);
    }

    @GetMapping("/loginOut")
    @IgnoreCheck
    public Response loginOut(){
        ShiroUtils.logout();
        return Response.ok();
    }

    @GetMapping("/getName")
    @RequiresPermissions("test:getName")
    public Response getName(){
        return Response.ok("猜猜我是谁--get");
    }
    @GetMapping("/getMessageGet")
    public Response getMessageGet(){
        return Response.ok("haha哈哈哈--get");
    }
    @PutMapping("/getMessagePut")
    public Response getMessagePut(){
        return Response.ok("haha哈哈哈--put");
    }
    @PostMapping("/getMessagePost")
    @IgnoreCheck
    public Response getMessagePost(){
        return Response.ok("haha哈哈哈-post");
    }
    @DeleteMapping("/getMessageDelete")
    public Response getMessageDelete(){
        return Response.ok("haha哈哈哈-delete");
    }
    @RequestMapping("/getMessageRequest")
    public Response getMessageRequest(){
        return Response.ok("haha哈哈哈-request");
    }
    @GetMapping("/register")
    @IgnoreCheck
    public Response register(String username,String password){
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        usersRepository.saveAndFlush(user);

        return Response.ok(user);
    }
    @GetMapping("/needLogin")
    @IgnoreCheck
    public Response needLogin(){
        return Response.error("没有登录");
    }
}
