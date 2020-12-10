package com.jenkin.menuservice.controller;


import com.jenkin.common.entity.Response;
import com.jenkin.common.utils.ShiroUtils;
import org.springframework.web.bind.annotation.*;


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
public class MenuController {


    @GetMapping("/loginOut")

    public Response loginOut(){
        ShiroUtils.logout();
        return Response.ok();
    }


}
