package com.jenkin.codegenerator.generate.service;

import com.jenkin.codegenerator.entity.CodeGenerateInfo;
import com.jenkin.common.entity.dtos.generate.TableInfoDto;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author jenkin
 * @className GenerateService
 * @description TODO
 * @date 2020/12/11 17:23
 */
public interface GenerateService {
    /**
     * 获取数据库里面的所有表信息
     * @return
     */
    List<TableInfoDto> listDbTables();

    /**
     * 获取还未创建的表信息
     * @return
     */
    List<TableInfoDto> listUnCreateTables();

    /**
     * 保存创建表的元数据
     * @param tableInfos
     */
    void saveTableInfo(List<TableInfoDto> tableInfos);

    /**
     * 创建表
     * @param tableInfos
     */
    void createTable(List<TableInfoDto> tableInfos);

    /**
     * 生成代码
     * @param codeGenerateInfos
     * @param outputStream
     */
    void generateCode(List<CodeGenerateInfo> codeGenerateInfos, OutputStream outputStream);

    /**
     * 获取字符集和排序
     * @return
     */
    Map<String,List<String>> listCollation();

    /**
     * 删除表信息
     * @param ids
     */
    void removeTableByIds(List<Integer> ids);
}
