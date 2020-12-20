package com.jenkin.codegenerator.entity;

import java.util.HashMap;
import java.util.Map;

public class JavaToMysqlType {
    public static Map<String, MySqlTypeConstant> javaToMysqlTypeMap = new HashMap<String, MySqlTypeConstant>();
    public static Map<String, MysqlType> mysqlToJavaTypeMap = new HashMap<String, MysqlType>();
    static {
        javaToMysqlTypeMap.put("class java.lang.String", MySqlTypeConstant.VARCHAR);
        javaToMysqlTypeMap.put("class java.lang.Long", MySqlTypeConstant.BIGINT);
        javaToMysqlTypeMap.put("class java.lang.Integer", MySqlTypeConstant.INT);
        javaToMysqlTypeMap.put("class java.lang.Boolean", MySqlTypeConstant.BIT);
        javaToMysqlTypeMap.put("class java.math.BigInteger", MySqlTypeConstant.BIGINT);
        javaToMysqlTypeMap.put("class java.lang.Float", MySqlTypeConstant.FLOAT);
        javaToMysqlTypeMap.put("class java.lang.Double", MySqlTypeConstant.DOUBLE);
        javaToMysqlTypeMap.put("class java.math.BigDecimal", MySqlTypeConstant.DECIMAL);
        javaToMysqlTypeMap.put("class java.sql.Date", MySqlTypeConstant.DATE);
        javaToMysqlTypeMap.put("class java.util.Date", MySqlTypeConstant.DATE);
        javaToMysqlTypeMap.put("class java.sql.Timestamp", MySqlTypeConstant.DATETIME);
        javaToMysqlTypeMap.put("class java.sql.Time", MySqlTypeConstant.TIME);
        javaToMysqlTypeMap.put("long", MySqlTypeConstant.BIGINT);
        javaToMysqlTypeMap.put("int", MySqlTypeConstant.INT);
        javaToMysqlTypeMap.put("boolean", MySqlTypeConstant.BIT);
        javaToMysqlTypeMap.put("float", MySqlTypeConstant.FLOAT);
        javaToMysqlTypeMap.put("double", MySqlTypeConstant.DOUBLE);
        javaToMysqlTypeMap.put("byte", MySqlTypeConstant.TINYINT);
        javaToMysqlTypeMap.put("short", MySqlTypeConstant.SMALLINT);
        javaToMysqlTypeMap.put("char", MySqlTypeConstant.VARCHAR);
    }
    static {

        mysqlToJavaTypeMap.put("VARCHAR", new MysqlType("VARCHAR","java.lang.String",255,null,false,true));
        mysqlToJavaTypeMap.put("TEXT", new MysqlType("TEXT","java.lang.String",255,null,false,true));
        mysqlToJavaTypeMap.put("JSON", new MysqlType("JSON","java.lang.String",null,null,false,true));
        mysqlToJavaTypeMap.put("BIGINT", new MysqlType("BIGINT","java.lang.Long",11,null,false));
        mysqlToJavaTypeMap.put("INT", new MysqlType("INT","java.lang.Integer",11,null,true));
        mysqlToJavaTypeMap.put("BIT", new MysqlType("BIT","java.lang.Boolean",1,null,false));
        mysqlToJavaTypeMap.put("TINYINT", new MysqlType("TINYINT","java.lang.Boolean",1,null,false));
        mysqlToJavaTypeMap.put("DOUBLE", new MysqlType("DOUBLE","java.lang.Double",9,2,false));
        mysqlToJavaTypeMap.put("FLOAT", new MysqlType("FLOAT","java.lang.Float",9,2,false));
        mysqlToJavaTypeMap.put("DECIMAL", new MysqlType("DECIMAL","java.math.BigDecimal",9,2,false));
        mysqlToJavaTypeMap.put("DATE", new MysqlType("DATE","java.util.Date",null,null,false));
        mysqlToJavaTypeMap.put("DATETIME", new MysqlType("DATETIME","java.time.LocalDateTime",null,null,false));
        mysqlToJavaTypeMap.put("TIME", new MysqlType("TIME","java.util.Date",null,null,false));



    }
}
