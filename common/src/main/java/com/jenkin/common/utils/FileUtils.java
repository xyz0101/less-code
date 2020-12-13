package com.jenkin.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/13 15:19
 * @description：文件操作工具类
 * @modified By：
 * @version: 1.0
 */
public class FileUtils {

    /**
     * 删除文件
     * @param files
     */
    public static void deleteFile(File... files) {
        if (!ArrayUtil.isEmpty(files)) {
            for (File file : files) {
                if (file!=null) {
                    FileUtil.del(file);
                }
            }
        }
    }

    /**
     * 关闭流
     * @param closeables
     */
    public static void closeStream(Closeable... closeables) {
        if (!ArrayUtil.isEmpty(closeables)) {
            for (Closeable closeable : closeables) {
                if (closeable!=null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
