package com.jenkin.fileservice.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/6/28 14:58
 * @menu
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class PoiUtils {
    public static void main(String[] args) throws Exception {
//        List<Map<String, String>> maps = readTable(new FileInputStream("C:\\Users\\admin\\Documents\\测试\\店铺保险汇总-京津自营(1).xlsx"), 3);

//        System.out.println(maps);

    }
    public static List<Map<String,String>> readTable(InputStream ips,int contentStartIndex) throws Exception {
        List<Map<String,String>> list = new ArrayList<>();
        XSSFWorkbook wb = new XSSFWorkbook(ips);
        XSSFSheet sheet = wb.getSheetAt(0);
        int i=1;
        for (Iterator<Row> ite = sheet.rowIterator(); ite.hasNext(); ) {
            XSSFRow row = (XSSFRow) ite.next();
            if(i<contentStartIndex){
                i++;
                continue;
            }
            Map<String,String> map = new HashMap<>();
            for (Iterator<Cell> itet = row.cellIterator(); itet.hasNext(); ) {
                XSSFCell cell = (XSSFCell) itet.next();
                String key = cell.getAddress().toString().replaceAll(i+"","");
                CellType cellTypeEnum = cell.getCellType();
                    if(CellType.BOOLEAN== cellTypeEnum) {
                        //得到Boolean对象的方法
                        map.put(key,String.valueOf(cell.getBooleanCellValue()).trim());
                    }else if(CellType.NUMERIC==cellTypeEnum){
                        //先看是否是日期格式
                        if (DateUtil.isCellDateFormatted(cell, null)) {
                            //读取日期格式
                            map.put(key,String.valueOf(cell.getDateCellValue()).trim());
                        } else {
                            //读取数字
                            map.put(key,String.valueOf(cell.getNumericCellValue()).trim());
                        }
                    }else if(CellType.FORMULA==cellTypeEnum){
                        //读取公式
                        map.put(key,cell.getRawValue().trim());
                    }else if(CellType.STRING==cellTypeEnum){
                        //读取String
                        map.put(key,cell.getRichStringCellValue().toString().trim());
                    }else{
                        map.put(key,cell.getRawValue()==null?null:cell.getRawValue().trim());
                    }

            }
            list.add(map);
            i++;
        }
        return list;
    }
}
