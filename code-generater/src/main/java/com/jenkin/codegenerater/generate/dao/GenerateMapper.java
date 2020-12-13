package com.jenkin.codegenerater.generate.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jenkin.codegenerater.entity.ColumnInfo;
import com.jenkin.codegenerater.entity.TableInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GenerateMapper extends BaseMapper<Object> {

    @Select("SELECT\n" +
            "    *\n" +
            "FROM\n" +
            "    information_schema.`TABLES`\n" +
            "WHERE\n" +
            "    TABLE_SCHEMA = #{dbName} ")
    List<TableInfo> listTables(@Param("dbName") String dbName);

    @Select("SELECT\n" +
            "    *\n" +
            "FROM\n" +
            "    information_schema.`TABLES`\n" +
            "WHERE\n" +
            "    TABLE_SCHEMA = #{dbName} and table_name = #{tableName}")
    TableInfo getOneTable(@Param("dbName") String dbName,@Param("tableName") String tableName);
    @Select("SELECT\n" +
            "\tIF(column_key='PRI',true,false) as is_id,\n" +
            "\tIF(is_nullable='YES',true,false) as is_null,\n" +
            "\tIF(extra='auto_increment',true,false) as is_auto_inc,\n" +
            "\t column_name as `name`,\n" +
            "\tIF(CHARACTER_maximum_length is NULL,NUMERIC_precision,CHARACTER_maximum_length) as `length`,\n" +
            "\t DATA_TYPE as `type`,\n" +
            "\t NUMERIC_scale as `decimal_Length`,\n" +
            "\t COLUMN_default as `default_Value`,\n" +
            "\t character_set_name as `encode`,\n" +
            "\t\tcollation_name as `sort`,\n" +
            "\tcolumn_comment as `comment`\n" +
            "\t \n" +
            "FROM\n" +
            "\tinformation_schema. COLUMNS\n" +
            "WHERE\n" +
            "\ttable_schema =#{dbName}\n" +
            "AND table_name = #{tableName} ")
    List<ColumnInfo> listColunms(@Param("dbName") String dbName, @Param("tableName") String tableName);


}
