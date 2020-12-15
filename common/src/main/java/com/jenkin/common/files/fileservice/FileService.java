package com.jenkin.common.files.fileservice;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * @author jenkin
 * @date 2020年12月15日16:09:42
 */
public interface FileService {

    /**
     *  上传文件
     * @param multipartFile
     * @return 文件路径
     */
    String uploadFile(MultipartFile multipartFile);

    /**
     * 上传本地文件
     *
     * @param file
     * @return
     */
    String uploadFile(File file);

    /**
     * 根据流上传文件
     *
     * @param inputStream
     * @param objectName  保存的文件名
     * @return
     */
    String uploadFile(InputStream inputStream, String objectName);

    /**
     * 获取文件流
     *
     * @param objectName
     * @return
     */
    InputStream getFile(String objectName);

    /**
     * 删除文件
     *
     * @param objectName
     */
    void deleteFile(String objectName);
}
