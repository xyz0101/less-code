package com.jenkin.systemservice.controller;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.anno.IgnoreCheck;
import com.jenkin.common.anno.MyPermission;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.dtos.system.UserDto;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.system.UserQo;
import com.jenkin.common.shiro.AESUtil;
import com.jenkin.common.shiro.token.MyToken;
import com.jenkin.common.utils.ShiroUtils;
import com.jenkin.systemservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

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
    @MyPermission(value = "sys:getuser")
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
    @IgnoreCheck
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

    @PostMapping("/login")
    @IgnoreCheck
    public Response login(@RequestHeader String info){
        byte[] decrypt2 = rsa.decrypt(info, KeyType.PrivateKey);
//        byte[] decrypt2 = info.getBytes();
        String userStr = new String(decrypt2);
        UserDto userDto = JSON.parseObject(userStr, UserDto.class);
        UserDto userEntityNoPermissionStr = ShiroUtils.removeSentiveMessage(userDto);
        userEntityNoPermissionStr.setSalt(UUID.randomUUID().toString()+
                UUID.randomUUID().toString()+UUID.randomUUID().toString()+UUID.randomUUID().toString());
        userDto.setLastUpdateDate(new Date());
        userDto.setCreationDate(new Date());
        String encrypt = AESUtil.encrypt(JSON.toJSONString(userEntityNoPermissionStr));
        MyToken myToken = new MyToken(userDto);
        myToken.setToken(encrypt);
        ShiroUtils.login(myToken);
        return Response.ok(encrypt);
    }

    @GetMapping("/getPublicKey")
    @IgnoreCheck
    public Response<PublicKey> getPublicKey(){
        PublicKey publicKey = rsa.getPublicKey();
        return Response.ok(publicKey );
    }





}
