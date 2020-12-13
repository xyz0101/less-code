package com.jenkin.codegenerater.generate.service.impl;

import com.jenkin.codegenerater.entity.CodeGenerateInfo;
import com.jenkin.codegenerater.entity.ColumnInfo;
import com.jenkin.codegenerater.entity.TableInfo;
import com.jenkin.codegenerater.generate.dao.GenerateMapper;
import com.jenkin.codegenerater.generate.service.GenerateService;
import com.jenkin.codegenerater.generate.util.CodeGenerator;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.utils.Redis;
import com.jenkin.common.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jenkin
 * @className GenerateServiceImpl
 * @description TODO
 * @date 2020/12/11 17:23
 */
@Service
@Slf4j
public class GenerateServiceImpl implements GenerateService {
    public static final String DEFAULT_DB = "less-code";
    private static final String UN_CREATE_TABLE_CACHE_KEY="uncreate:table:";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Resource
    private GenerateMapper generateMapper;
    @Autowired
    private Redis redis;
    /**
     * 获取数据库里面的所有表信息
     *
     * @return
     */
    @Override
    public List<TableInfo> listDbTables() {
        List<TableInfo> tableInfos = generateMapper.listTables(DEFAULT_DB );
        for (TableInfo tableInfo : tableInfos) {
            List<ColumnInfo> columInfos = generateMapper.listColunms(DEFAULT_DB, tableInfo.getTableName());
            tableInfo.setColumns(columInfos);
        }

        return tableInfos;
    }

    /**
     * 获取还未创建的表信息
     *
     * @return
     */
    @Override
    public List<TableInfo> listUnCreateTables() {

        return (List<TableInfo>) redis.lGet(UN_CREATE_TABLE_CACHE_KEY+ ShiroUtils.getUserCode(),0,-1);
    }

    /**
     * 保存创建表的元数据
     *
     * @param tableInfos
     */
    @Override
    public void saveTableInfo(List<TableInfo> tableInfos) {

        redis.lSet(UN_CREATE_TABLE_CACHE_KEY+ ShiroUtils.getUserCode(),tableInfos);
    }

    /**
     * 创建表
     *
     * @param tableInfos
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTable(List<TableInfo> tableInfos) {
        List<String> res = generateTable(tableInfos);
        if (!CollectionUtils.isEmpty(res)) {
            for (String re : res) {
                log.info("执行SQL：{}",re);
                jdbcTemplate.execute(re);
            }
        }
    }

    /**
     * 生成代码
     *
     * @param codeGenerateInfos
     * @param outputStream
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateCode(List<CodeGenerateInfo> codeGenerateInfos, OutputStream outputStream) {
        if (!CollectionUtils.isEmpty(codeGenerateInfos)) {
            List<String> tables = new ArrayList<>();
            try {
                for (CodeGenerateInfo codeGenerateInfo : codeGenerateInfos) {
                    TableInfo oneTable = generateMapper.getOneTable(DEFAULT_DB, codeGenerateInfo.getTableInfo().getTableName());
                    //如果表已存在，不用创建表
                    if (oneTable!=null) {
                        //生成代码
                        CodeGenerator.generateCode(codeGenerateInfo,outputStream);
                    }else{
                        //创建表
                        StringBuilder sql = generateOneTable(codeGenerateInfo.getTableInfo());
                        tables.add(codeGenerateInfo.getTableInfo().getTableName());
                        log.info("当前SQL：{}",sql);
                        jdbcTemplate.execute(sql.toString());
                        //生成代码
                        CodeGenerator.generateCode(codeGenerateInfo,outputStream);
                    }
                }
            }catch (Exception e){
                tables.forEach(item->{jdbcTemplate.execute("drop table `"+item+"`");});
                e.printStackTrace();
            }

        }
    }


    private static List<String> generateTable(List<TableInfo> tableInfos) {
        List<String> res = new ArrayList<>();
        tableInfos.forEach(item->{
           StringBuilder sql = generateOneTable(item);
            res.add(sql.toString());
        });
        return res;
    }

    private static StringBuilder generateOneTable(TableInfo item) {
        String id = null;
        StringBuilder sql = new StringBuilder();
        sql.append("create table `").append(item.getTableName()).append("` ").append("(");
        for (ColumnInfo colum : item.getColumns()) {
            if (colum.getIsId()){
                id = colum.getName();
            }
            if (!colum.getType().equals("VARCHAR")) {
                if(!StringUtils.isEmpty(colum.getDefaultValue())) {
                    colum.setDefaultValue("'" + colum.getDefaultValue() + "'");
                }
            }
            colum.setComments("'"+colum.getComments()+"'");

            sql.append(" `").append(colum.getName()).append("` ").append(colum.getType());
            if (colum.getLength()!=null&&colum.getLength()>0) {
                sql.append("(")
                        .append(colum.getLength());
                if(colum.getDecimalLength()!=null&&colum.getDecimalLength()>0){
                    sql.append(",").append(colum.getDecimalLength());
                }
                sql.append(")");
            }
            if (colum.getType().equals("VARCHAR")) {
                sql.append(" CHARACTER SET ").append(colum.getEncode()).append(" COLLATE ")
                        .append(colum.getSort()).append(" NULL ");
            }
            if(!StringUtils.isEmpty(colum.getDefaultValue())) {
                sql.append(" DEFAULT ")
                        .append(StringUtils.isEmpty(colum.getDefaultValue()) ? " NULL " : colum.getDefaultValue());
            }
            sql.append(colum.getIsAutoInc()?" AUTO_INCREMENT ":"")
                    .append(" COMMENT ").append(colum.getComments())
                    .append(" ,");
        }
        if (id==null) {
            sql.deleteCharAt(sql.length()-1);
        }else{
            sql.append(" PRIMARY KEY (`").append(id).append("`) ");
        }
        sql.append(") ").append(" ENGINE = ").append(item.getEngine()).append(" CHARACTER SET = ")
                .append(item.getEncode()).append(" COLLATE = ")
                .append(item.getTableCollation()).append(" ROW_FORMAT = Dynamic COMMENT '").append(item.getTableComments()).append("';");
        return sql;
    }

}
