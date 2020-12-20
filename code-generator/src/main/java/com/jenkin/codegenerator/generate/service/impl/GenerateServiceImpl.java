package com.jenkin.codegenerator.generate.service.impl;

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Maps;
import com.jenkin.codegenerator.entity.CodeGenerateInfo;
import com.jenkin.codegenerator.entity.JavaToMysqlType;
import com.jenkin.codegenerator.generate.service.ColumnInfoService;
import com.jenkin.codegenerator.generate.service.TableInfoService;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.generate.ColumnInfoDto;
import com.jenkin.common.entity.dtos.generate.TableInfoDto;
import com.jenkin.codegenerator.generate.dao.GenerateMapper;
import com.jenkin.codegenerator.generate.service.GenerateService;
import com.jenkin.codegenerator.generate.util.CodeGenerator;
import com.jenkin.common.entity.pos.BasePo;
import com.jenkin.common.entity.pos.generate.ColumnInfoPo;
import com.jenkin.common.entity.pos.generate.TableInfoPo;
import com.jenkin.common.exception.ExceptionEnum;
import com.jenkin.common.exception.LscException;
import com.jenkin.common.utils.BeanUtils;
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
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private ColumnInfoService columnInfoService;

    /**
     * 获取数据库里面的所有表信息
     *
     * @return
     */
    @Override
    public List<TableInfoDto> listDbTables() {
        List<TableInfoDto> tableInfos = generateMapper.listTables(DEFAULT_DB );
        for (TableInfoDto tableInfo : tableInfos) {
            tableInfo.setUnCreateFlag(false);
            List<ColumnInfoDto> columInfos = generateMapper.listColunms(DEFAULT_DB, tableInfo.getTableName());
            if (columInfos!=null) {
                columInfos.forEach(item->{
                    item.setType(item.getType()==null?null:item.getType().toUpperCase());
                    item.setJavaColName(com.baomidou.mybatisplus.core.toolkit.StringUtils.underlineToCamel(item.getName()));
                    log.info(item.getType());
                    String javaType = JavaToMysqlType.mysqlToJavaTypeMap.get(item.getType()).getJavaType();
                    javaType=javaType.substring(javaType.lastIndexOf(".")+1);
                    item.setJavaType(javaType);
                });
            }
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
    public List<TableInfoDto> listUnCreateTables() {
        MyQueryWrapper<TableInfoPo> myQueryWrapper = new MyQueryWrapper<>();
        myQueryWrapper.eq(BasePo.Fields.createdBy,ShiroUtils.getUserCode());
        myQueryWrapper.eq(TableInfoPo.Fields.unCreateFlag,true);
        List<TableInfoPo> list = tableInfoService.list(myQueryWrapper);

        Map<Integer,List<ColumnInfoDto>> columns = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            Set<Integer> collect = list.stream().map(item -> item.getId()).collect(Collectors.toSet());
            MyQueryWrapper<ColumnInfoPo> queryWrapper = new MyQueryWrapper<>();
            queryWrapper.in(ColumnInfoPo.Fields.tableId,collect);
            List<ColumnInfoPo> columnInfoPos = columnInfoService.list(queryWrapper);
            if (!CollectionUtils.isEmpty(columnInfoPos)) {
                columnInfoPos.forEach(item->{
                    item.setType(item.getType()==null?null:item.getType().toUpperCase());
                    List<ColumnInfoDto> columnInfos =
                            columns.get(item.getTableId())==null?new ArrayList<>():columns.get(item.getTableId());
                    columnInfos.add(BeanUtils.map(item,ColumnInfoDto.class));
                    columns.put(item.getTableId(),columnInfos);
                });
            }

        }
        List<TableInfoDto> tableInfoDtos = BeanUtils.mapList(list, TableInfoDto.class);
        tableInfoDtos.forEach(item->{
            item.setColumns(columns.get(item.getId()));
            item.setUnCreateFlag(true);
        });


        return  tableInfoDtos;


    }

    /**
     * 保存创建表的元数据
     *
     * @param tableInfos
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTableInfo(List<TableInfoDto> tableInfos) {

        for (TableInfoDto tableInfo : tableInfos) {
            tableInfo.setUnCreateFlag(true);
            TableInfoPo map = BeanUtils.map(tableInfo, TableInfoPo.class);
            tableInfoService.saveOrUpdate(map);
            MyQueryWrapper<ColumnInfoPo> myQueryWrapper = new MyQueryWrapper<>();
            myQueryWrapper.eq(ColumnInfoPo.Fields.tableId,map.getId());
            columnInfoService.remove(myQueryWrapper);
            List<ColumnInfoPo> columnInfoPos = BeanUtils.mapList(tableInfo.getColumns(), ColumnInfoPo.class);
            columnInfoPos.forEach(item->item.setTableId(map.getId()));
            columnInfoService.saveOrUpdateBatch(columnInfoPos);
        }

    }

    /**
     * 创建表
     *
     * @param tableInfos
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTable(List<TableInfoDto> tableInfos) {

        tableInfos.forEach(item->item.setUnCreateFlag(false));
        try {
            List<String> res = generateTable(tableInfos);
            if (!CollectionUtils.isEmpty(res)) {
                int i=0;
                for (String re : res) {

                    log.info("执行SQL：{}",re);
                    jdbcTemplate.execute(re);
                    TableInfoDto tableInfoDto = tableInfos.get(i);
                    tableInfoService.saveOrUpdate(BeanUtils.map(tableInfoDto,TableInfoPo.class));
                    i++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw  new LscException(ExceptionEnum.SQL_ERROR_EXCEPTION);
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
                    TableInfoDto oneTable = generateMapper.getOneTable(DEFAULT_DB, codeGenerateInfo.getTableInfo().getTableName());
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
                throw e;
            }

        }
    }


    private static List<String> generateTable(List<TableInfoDto> tableInfos) {
        List<String> res = new ArrayList<>();
        tableInfos.forEach(item->{
           StringBuilder sql = generateOneTable(item);
            res.add(sql.toString());
        });
        return res;
    }

    private static StringBuilder generateOneTable(TableInfoDto item) {
        String id = null;
        StringBuilder sql = new StringBuilder();
        sql.append("create table `").append(item.getTableName()).append("` ").append("(");
        for (ColumnInfoDto colum : item.getColumns()) {
            if (colum.getIdFlag()){
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
                        .append(colum.getSort()).append( colum.getNullFlag()? " NULL ":" NOT NULL ");
            }
            if(!StringUtils.isEmpty(colum.getDefaultValue())) {
                sql.append(" DEFAULT ")
                        .append(StringUtils.isEmpty(colum.getDefaultValue()) ? " NULL " : colum.getDefaultValue());
            }
            sql.append(colum.getAutoIncFlag()?" AUTO_INCREMENT ":"")
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


    /**
     * 获取字符集和排序
     *
     * @return
     */
    @Override
    public Map<String, List<String>> listCollation() {
        Map<String, List<String>> res = new HashMap<>();
        jdbcTemplate.query("SHOW COLLATION", rs -> {

                String collation = rs.getString(1);
                String charset = rs.getString(2);
                List<String> colls = res.get(charset);
                if (colls==null){
                    colls=new ArrayList<>();
                }
                colls.add(collation);
                res.put(charset,colls);

        });
        return res;
    }

    /**
     * 删除表信息
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTableByIds(List<Integer> ids) {
        tableInfoService.removeByIds(ids);
        MyQueryWrapper<ColumnInfoPo> myQueryWrapper = new MyQueryWrapper<>();
        myQueryWrapper.in(ColumnInfoPo.Fields.tableId,ids);
        columnInfoService.remove(myQueryWrapper);
    }
}
