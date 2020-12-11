package com.jenkin.menuservice.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.anno.IgnoreCheck;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;
import com.jenkin.common.utils.ShiroUtils;
import com.jenkin.menuservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/10 21:15
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    RSA rsa = new RSA();

    @Autowired
    private UserService userService;


    @PostMapping("/listUserByPage")
    @ApiOperation("分页获取用户信息")
    public Response<Page<UserDto>> listUserByPage(@RequestBody BaseQo<UserQo> user){
        Page<UserDto> userDtoPage = userService.listByPage(user);
        return Response.ok(userDtoPage);
    }
    @GetMapping("/getSingleUserByCode")
    @ApiOperation("根据用户编号获取用户信息")
    public Response<UserDto> getSingleUserByCode(String code){
        if (StringUtils.isEmpty(code)) {
            return Response.ok();
        }
        UserDto byCode = userService.getByCode(code);
        return Response.ok(byCode);
    }
    @PostMapping("/saveUser")
    @ApiOperation("保存用户信息")
    public Response saveUser(@RequestBody UserDto userDto){
        UserDto userDto1 = userService.saveUserInfo(userDto);
        return Response.ok();
    }

    @PostMapping("/register")
    @ApiOperation("注册新用户")
    public Response register(@RequestBody UserDto userDto){
        userService.register(userDto);
        return Response.ok();
    }
    @PostMapping("/deleteUser")
    @ApiOperation("删除用户")
    public Response deleteUser(@RequestBody Integer[] ids){
        userService.removeByIds(Arrays.asList(ids));
        return Response.ok();
    }

    @GetMapping("/login")
    @IgnoreCheck
    public Response login(@RequestHeader String info){
        byte[] decrypt2 = rsa.decrypt(info, KeyType.PrivateKey);
        String userStr = new String(decrypt2);
        UserDto userDto = JSON.parseObject(userStr, UserDto.class);
        Subject subject = ShiroUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userDto.getUserName(), userDto.getPassword());
        subject.login(token);
        UserDto userEntity = ShiroUtils.getUserEntityNoPermissionStr();
        return Response.ok(userEntity);
    }

    @GetMapping("/getPublicKey")
    @IgnoreCheck
    public Response<PublicKey> getPublicKey(){
        PublicKey publicKey = rsa.getPublicKey();
        return Response.ok(publicKey );
    }





}
