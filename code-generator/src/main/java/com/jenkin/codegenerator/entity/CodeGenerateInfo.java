package com.jenkin.codegenerator.entity;

import com.jenkin.common.entity.dtos.generate.TableInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/13 14:10
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@ApiModel("代码生成的实体类")
public class CodeGenerateInfo {
    @ApiModelProperty("数据库表的信息")
    private TableInfoDto tableInfo;
    @ApiModelProperty("项目模块名称，例如 codegenerate")
    private String projectModule="";
    @ApiModelProperty("基础包，默认是 com.jenkin")
    private String basePackageName ="com.jenkin";
    @ApiModelProperty("包名称，basePackageName+.+projectModule")
    private String packageName = StringUtils.isEmpty(projectModule)?basePackageName:basePackageName+"."+projectModule;
    @ApiModelProperty("实体类的基础包名称，结构为：entityPackageName+pos/dtos/qos/vos+moduleName")
    private String entityPackageName="com.jenkin.common.entity";
    @ApiModelProperty("作者 默认jenkin")
    private String author="jenkin";
    @ApiModelProperty("类名称")
    private String className;
    @ApiModelProperty("类名称，首字母小写")
    private String classNameLower;
    @ApiModelProperty("模块名称，比如user")
    private String moduleName;
    @ApiModelProperty("创建时间")
    private String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


}
