package com.jenkin.codegenerator.generate.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jenkin.codegenerator.entity.CodeGenerateInfo;
import com.jenkin.codegenerator.entity.JavaToMysqlType;
import com.jenkin.codegenerator.entity.MysqlType;
import com.jenkin.codegenerator.entity.TableInfo;
import com.jenkin.codegenerator.generate.service.GenerateService;
import com.jenkin.common.entity.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/listUnCreateTables")
    @ApiOperation("获取还未创建的表信息")
    public Response<List<TableInfo>> listUnCreateTables() {
        return Response.ok(generateService.listUnCreateTables());
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
     * @param codeGenerateInfos
     * @param response
     */
    @PostMapping("/generateCode")
    @ApiOperation("生成代码")
    public void generateCode(@RequestBody List<CodeGenerateInfo> codeGenerateInfos, HttpServletResponse response) {
        try {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=result.zip"   );
            generateService.generateCode(codeGenerateInfos,response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库字段和java字段的映射关系
     * @return
     */
    @ApiOperation("获取数据库字段和java字段的映射关系")
    @GetMapping("/getTypeInfo")
    public Response<Map<String, MysqlType>> getTypeInfo(){
        return Response.ok(JavaToMysqlType.mysqlToJavaTypeMap);
    }


    /**
     * 获取数据库字段和java字段的映射关系
     * @return
     */
    @ApiOperation("下划线转驼峰")
    @GetMapping("/underlineToCamel")
    public Response<String> underlineToCamel(String code){
        return Response.ok(StringUtils.underlineToCamel(code));
    }




}
