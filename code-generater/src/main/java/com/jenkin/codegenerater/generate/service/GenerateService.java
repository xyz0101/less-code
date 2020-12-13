package com.jenkin.codegenerater.generate.service;

import com.jenkin.codegenerater.entity.CodeGenerateInfo;
import com.jenkin.codegenerater.entity.TableInfo;

import java.io.OutputStream;
import java.util.List;

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
    List<TableInfo> listDbTables();

    /**
     * 获取还未创建的表信息
     * @return
     */
    List<TableInfo> listUnCreateTables();

    /**
     * 保存创建表的元数据
     * @param tableInfos
     */
    void saveTableInfo(List<TableInfo> tableInfos);

    /**
     * 创建表
     * @param tableInfos
     */
    void createTable(List<TableInfo> tableInfos);

    /**
     * 生成代码
     * @param codeGenerateInfos
     * @param outputStream
     */
    void generateCode(List<CodeGenerateInfo> codeGenerateInfos, OutputStream outputStream);

}
