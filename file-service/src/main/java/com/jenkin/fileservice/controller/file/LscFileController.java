package com.jenkin.fileservice.controller.file;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.qos.file.LscFileQo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.dtos.file.LscFileDto;
import com.jenkin.common.entity.pos.file.LscFilePo;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.fileservice.service.file.LscFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * @author ：jenkin
 * @date ：Created at 2021-01-03 20:11:43
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
public class LscFileController {

    @Autowired
    private LscFileService lscFileService;

    @GetMapping("/listAllData")
    @ApiOperation("获取所有信息，不分页")
    public Response< List<LscFileDto>> listAllData(){
        List<LscFilePo> res =  lscFileService.list();
        return Response.ok(BeanUtils.mapList(res,LscFileDto.class));
    }

    @PostMapping("/listByPage")
    @ApiOperation("分页获取信息")
    public Response<Page<LscFileDto>> listLscFileByPage(@RequestBody BaseQo<LscFileQo> qo){
        Page<LscFileDto>  lscFileDtoPage =  lscFileService.listByPage(qo);
        return Response.ok( lscFileDtoPage);
    }
    @GetMapping("/getSingleById")
    @ApiOperation("根据id获取信息")
    public Response< LscFileDto> getSingleById(Integer id){
        if (id==null) {
            return Response.ok();
        }
        LscFileDto byId =  lscFileService.getById(id);
        return Response.ok(byId);
    }
    @PostMapping("/save")
    @ApiOperation("保存信息")
    public Response save(@RequestBody  LscFileDto  lscFileDto){
        LscFileDto data =  lscFileService.saveLscFileInfo( lscFileDto);
        return Response.ok();
    }


    @PostMapping("/deleteById")
    @ApiOperation("删除")
    public Response deleteLscFile(@RequestBody Integer[] ids){
        lscFileService.removeByIds(Arrays.asList(ids));
        return Response.ok();
    }



}
