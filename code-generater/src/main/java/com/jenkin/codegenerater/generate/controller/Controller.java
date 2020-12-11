package com.jenkin.codegenerater.generate.controller;

import com.jenkin.codegenerater.entity.TableInfo;
import com.jenkin.codegenerater.generate.service.GenerateService;
import com.jenkin.common.entity.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author jenkin
 * @className Controller
 * @description TODO
 * @date 2020/12/11 17:22
 */
@RequestMapping("/generate")
@RestController
@CrossOrigin
@Api(tags = "代码生成器")
public class Controller {
    @Autowired
    private GenerateService generateService;
    /**
     * 获取数据库里面的所有表信息
     *
     * @return
     */
    @GetMapping("/listDbTables")
    @ApiOperation("获取数据库里面的所有表信息")
    public Response<List<TableInfo>> listDbTables() {
        return Response.ok(generateService.listDbTables());
    }

    /**
     * 获取还未创建的表信息
     *
     * @return
     */
    @GetMapping("/listDbTables")
    @ApiOperation("获取还未创建的表信息")
    public Response<List<TableInfo>> listUnCreateTables() {
        return Response.ok(generateService.listDbTables());
    }

    /**
     * 保存创建表的元数据
     *
     * @param tableInfos
     */
    @PostMapping("/saveTableInfo")
    @ApiOperation(("保存建表信息"))
    public Response saveTableInfo(@RequestBody List<TableInfo> tableInfos) {
        generateService.saveTableInfo(tableInfos);
        return Response.ok();
    }

    /**
     * 创建表
     *
     * @param tableInfos
     */
    @PostMapping("/createTable")
    @ApiOperation("创建表")
    public Response createTable(@RequestBody List<TableInfo> tableInfos) {
        generateService.createTable(tableInfos);
        return Response.ok();
    }

    /**
     * 生成代码
     *
     * @param tableInfos
     * @param response
     */
    @PostMapping("/generateCode")
    @ApiOperation("生成代码")
    public void generateCode(@RequestBody List<TableInfo> tableInfos, HttpServletResponse response) {
        try {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=result.zip"   );
            generateService.generateCode(tableInfos,response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
