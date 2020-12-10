package com.jenkin.common.utils;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import com.gitee.sunchenbin.mybatis.actable.utils.ColumnUtils;
import com.google.common.io.Files;
import com.jenkin.common.entity.pos.BasePo;
import lombok.Data;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.gitee.sunchenbin.mybatis.actable.command.JavaToMysqlType.javaToMysqlTypeMap;

/**
 * @author jenkin
 * @className GenarateSql
 * @description TODO
 * @date 2020/12/10 16:50
 */
public class GenarateSql {
    private static String defaultCharSet = "utf8mb4";
    private static String defaultCharSetCollect = "utf8mb4_bin";
    private static String path = System.getProperty("user.dir")+"\\common\\src\\main\\java\\com\\jenkin\\common\\entity\\pos";
    private static String packageName = "com.jenkin.common.entity.pos";
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(System.getProperty("user.dir"));
        List<TableBean> tableBeans= getTable();
        System.out.println(tableBeans);
        generateTable(tableBeans);
    }

    private static void generateTable(List<TableBean> tableBeans) {
        tableBeans.forEach(item->{
            String id = null;
            StringBuilder sql = new StringBuilder();
            sql.append("create table `").append(item.getTableName()).append("` ").append("(");
            for (ColumBean colum : item.getColums()) {
                if (colum.isId){
                    id = colum.getName();
                }
                if (!colum.getType().equals("VARCHAR")) {
                    if(!StringUtils.isEmpty(colum.getDefaultValue())) {
                        colum.setDefaultValue("'" + colum.getDefaultValue() + "'");
                    }
                }
                colum.setComment("'"+colum.getComment()+"'");

                sql.append(" `").append(colum.getName()).append("` ").append(colum.getType());
                    if (colum.length!=null&&colum.length>0) {
                            sql.append("(")
                                .append(colum.length).append(")");
                    }
                if (colum.getType().equals("VARCHAR")) {
                    sql.append(" CHARACTER SET ").append(defaultCharSet).append(" COLLATE ")
                            .append(defaultCharSetCollect).append(" NULL ");
                }
                        if(!StringUtils.isEmpty(colum.getDefaultValue())) {
                                sql.append(" DEFAULT ")
                                    .append(StringUtils.isEmpty(colum.getDefaultValue()) ? " NULL " : colum.getDefaultValue());
                        }
                       sql.append(colum.isAutoInc?" AUTO_INCREMENT ":"")
                        .append(" COMMENT ").append(colum.getComment())
                        .append(" ,");
            }
            if (id==null) {
                sql.deleteCharAt(sql.length()-1);
            }else{
                sql.append(" PRIMARY KEY (`").append(id).append("`) ");
            }
            sql.append(") ").append(" ENGINE = InnoDB CHARACTER SET = ")
                    .append(defaultCharSet).append(" COLLATE = ")
                    .append(defaultCharSetCollect).append(" ROW_FORMAT = Dynamic COMMENT '"+item.getComment()+"';");
            System.out.println(sql);
            System.out.println();
            System.out.println();
        });
    }

    private static List<TableBean> getTable() throws ClassNotFoundException {
        File file = new File(path);
        List<Class<?>> classList = new ArrayList<>();
        getClassList(file,classList);
        return  getTableList(classList);
    }

    private static List<TableBean> getTableList(List<Class<?>> classList) {
        List<TableBean> res = new ArrayList<>();
        for (Class<?> aClass : classList) {
            TableBean tableBean = new TableBean();
            tableBean.setColums(getColums(aClass));
            Table table = aClass.getDeclaredAnnotation(Table.class);
            String comment = table.comment();
            String name = table.name();
            tableBean.setComment(comment);
            tableBean.setTableName(name);
            res.add(tableBean);
        }
        return res;
    }

    private static List<ColumBean> getColums(Class<?> aClass) {
        List<ColumBean> res = new ArrayList<>();
        Field[] declaredFields1 = BasePo.class.getDeclaredFields();
        Field[] declaredFields2 = aClass.getDeclaredFields();
        Field[] fields = ArrayUtil.addAll(declaredFields1, declaredFields2);
        for (Field declaredField : fields) {
            if (declaredField.isAnnotationPresent(Column.class)) {
                ColumBean columBean = new ColumBean();
                Column declaredAnnotation = declaredField.getDeclaredAnnotation(Column.class);
                String cloName = declaredAnnotation.name();
                if (StringUtils.isEmpty(cloName)) {
                    cloName=declaredField.getName();
                    cloName=com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(cloName);
                }
                columBean.setComment(declaredAnnotation.comment());
                columBean.setName(cloName);
                MySqlTypeConstant type = getType(declaredAnnotation, declaredField);
                if (type==null) {
                    System.out.println(declaredField.getName());
                    continue;
                }
                columBean.setType(type.toString());
                columBean.setDecimalLength(type.getDecimalLengthDefault());
                columBean.setDefaultValue(declaredAnnotation.defaultValue().equals( ColumnUtils.DEFAULTVALUE)?"":declaredAnnotation.defaultValue());
                columBean.setLength(type.getLengthDefault());
                if (declaredField.isAnnotationPresent(IsKey.class)){
                    columBean.setId(true);
                }
                if (declaredField.isAnnotationPresent(IsAutoIncrement.class)){
                    columBean.setAutoInc(true);
                }
                res.add(columBean);
            }
        }
        return res;
    }

    private static MySqlTypeConstant getType(Column declaredAnnotation, Field declaredField) {
        if (declaredAnnotation.type()!= MySqlTypeConstant.DEFAULT) {
            return declaredAnnotation.type() ;
        }else{
            String s = "class " + declaredField.getType().getName();
            return javaToMysqlTypeMap.get(s) ;
        }

    }

    private static void getClassList(File file,List<Class<?>> classList) throws ClassNotFoundException {

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (file1.isFile()) {
                   String classpath = packageName+"."+ Files.getNameWithoutExtension(file1.getName());
                    Class<?> aClass = Class.forName(classpath);
                    if (aClass.isAnnotationPresent(Table.class)) {
                        classList.add(aClass);
                    }

                }else{
                    getClassList(file1,classList);
                }
            }
        }

    }

    @Data
    static class TableBean{
        private String tableName;
        private String engin;
        private String encode;
        private String sort;
        private String comment;
        private List<ColumBean> colums;
    }
    @Data
    static class ColumBean{
        private boolean isId;
        private boolean isAutoInc;
        private String name;
        private Integer length;
        private String type;
        private Integer decimalLength;
        private String defaultValue;
        private String encode;
        private String sort;
        private String comment;
    }

}
