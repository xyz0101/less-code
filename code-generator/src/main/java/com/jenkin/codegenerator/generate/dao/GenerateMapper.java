package com.jenkin.codegenerator.generate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenkin.common.entity.dtos.generate.ColumnInfoDto;
import com.jenkin.common.entity.dtos.generate.TableInfoDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GenerateMapper extends BaseMapper<Object> {

    @Select("SELECT " +
            "    t.*, t.table_comment table_comments  " +
            "FROM " +
            "    information_schema.`TABLES` t " +
            "WHERE " +
            "    TABLE_SCHEMA = #{dbName} ")
    List<TableInfoDto> listTables(@Param("dbName") String dbName);

    @Select("SELECT " +
            "    t.* , t.table_comment table_comments " +
            "FROM " +
            "    information_schema.`TABLES` t " +
            "WHERE " +
            "    TABLE_SCHEMA = #{dbName} and table_name = #{tableName}")
    TableInfoDto getOneTable(@Param("dbName") String dbName, @Param("tableName") String tableName);
    @Select("SELECT " +
            " IF(column_key='PRI',true,false) as id_flag, " +
            " IF(is_nullable='YES',true,false) as null_flag, " +
            " IF(extra='auto_increment',true,false) as auto_inc_flag, " +
            "  column_name as `name`, " +
            " IF(CHARACTER_maximum_length is NULL,NUMERIC_precision,CHARACTER_maximum_length) as `length`, " +
            "  DATA_TYPE as `type`, " +
            "  NUMERIC_scale as `decimal_Length`, " +
            "  COLUMN_default as `default_Value`, " +
            "  character_set_name as `encode`, " +
            "  collation_name as `sort`, " +
            " column_comment as `comment` ," +
            " column_comment as `comments` " +
            "   " +
            "FROM " +
            " information_schema. COLUMNS " +
            "WHERE " +
            " table_schema =#{dbName} " +
            "AND table_name = #{tableName} ")
    List<ColumnInfoDto> listColunms(@Param("dbName") String dbName, @Param("tableName") String tableName);




}
