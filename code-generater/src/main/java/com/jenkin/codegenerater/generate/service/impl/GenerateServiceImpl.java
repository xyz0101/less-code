package com.jenkin.codegenerater.generate.service.impl;

import com.jenkin.codegenerater.entity.TableInfo;
import com.jenkin.codegenerater.generate.service.GenerateService;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

/**
 * @author jenkin
 * @className GenerateServiceImpl
 * @description TODO
 * @date 2020/12/11 17:23
 */
@Service
public class GenerateServiceImpl implements GenerateService {
    /**
     * 获取数据库里面的所有表信息
     *
     * @return
     */
    @Override
    public List<TableInfo> listDbTables() {
        return null;
    }

    /**
     * 获取还未创建的表信息
     *
     * @return
     */
    @Override
    public List<TableInfo> listUnCreateTables() {
        return null;
    }

    /**
     * 保存创建表的元数据
     *
     * @param tableInfos
     */
    @Override
    public void saveTableInfo(List<TableInfo> tableInfos) {

    }

    /**
     * 创建表
     *
     * @param tableInfos
     */
    @Override
    public void createTable(List<TableInfo> tableInfos) {

    }

    /**
     * 生成代码
     *
     * @param tableInfos
     * @param outputStream
     */
    @Override
    public void generateCode(List<TableInfo> tableInfos, OutputStream outputStream) {

    }
}
