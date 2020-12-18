package com.jenkin.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/13 15:19
 * @description：文件操作工具类
 * @modified By：
 * @version: 1.0
 */
@Slf4j
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
     * 生成文件名
     *
     * @param suffix 文件后缀或者文件名带后缀
     * @return
     */
    public static String generatorName(String suffix) {
        Assert.notNull(suffix, "文件后缀不能为空");
        if (suffix.contains(".")) {
            return generatorName() + suffix.substring(suffix.lastIndexOf("."));
        }
        return generatorName() + "." + suffix;
    }
    /**
     * 获取content-type
     *
     * @param suffix
     * @return
     */
    public static String getContentType(String suffix) {
        ContentType contentType;
        if (suffix == null) {
            contentType = ContentType.APPLICATION_OCTET_STREAM;
            return contentType.toString();
        } else if (suffix.contains(".")) {
            suffix = suffix.substring(suffix.lastIndexOf(".") + 1);
        }
        if ("png".equals(suffix)) {
            contentType = ContentType.IMAGE_PNG;
        } else if ("jpg".equals(suffix)) {
            contentType = ContentType.IMAGE_JPEG;
        } else if ("jpeg".equals(suffix)) {
            contentType = ContentType.IMAGE_JPEG;
        } else if ("bmp".equals(suffix)) {
            contentType = ContentType.IMAGE_BMP;
        } else if ("gif".equals(suffix)) {
            contentType = ContentType.IMAGE_GIF;
        } else {
            contentType = ContentType.APPLICATION_OCTET_STREAM;
        }
        return contentType.toString();
    }
    public static String generatorName() {
        String format = "yyyy/MM/dd/";
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format)) +
                UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
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

    /**
     * 将InputStream写入本地文件
     * @param destination 写入本地目录
     * @param input 输入流
     * @throws IOException IOException
     */
    public static void writeToLocal(File destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();

    }

    /**
     * 文件下载包装方法，把文件流包装到response
     * @param fileName
     * @param inputStream
     * @param response
     */
    public static void downloadFile(String fileName, InputStream inputStream, HttpServletResponse response) {
        BufferedInputStream buffInputStream = null;
        OutputStream outputStream = null;
        try {

            response.setHeader("content-type", FileUtils.getContentType(fileName));
            response.setContentType( FileUtils.getContentType(fileName));
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            outputStream = response.getOutputStream();
            buffInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[4096];
            int num;
            while ((num = buffInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, num);
            }
            outputStream.flush();

        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                if (buffInputStream != null) {
                    buffInputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }
}
