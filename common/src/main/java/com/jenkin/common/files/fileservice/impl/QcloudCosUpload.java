package com.jenkin.common.files.fileservice.impl;

import com.jenkin.common.files.fileservice.FileService;
import com.jenkin.common.files.properties.FileProperties;
import com.jenkin.common.utils.FileUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author jenkin
 * @date 2020年12月15日16:09:42
 */
@Data
@Slf4j
public class QcloudCosUpload implements FileService {

    private FileProperties fileProperties;
    private COSClient cosClient;

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        final String key = fileProperties.getPrePath() + FileUtils.generatorName(multipartFile.getOriginalFilename());
        try (InputStream inputStream = multipartFile.getInputStream()) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());
            cosClient.putObject(fileProperties.getBucketName(), key, inputStream, objectMetadata);
            return key;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String uploadFile(File file) {
        final String key = fileProperties.getPrePath() + FileUtils.generatorName(file.getName());
        cosClient.putObject(fileProperties.getBucketName(), key, file);
        return key;
    }

    @Override
    public String uploadFile(InputStream inputStream, String objectName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        int contentLength = 0;
        // 设置输入流长度为500
        try {
            contentLength = inputStream.available();
            objectMetadata.setContentLength(contentLength);
        } catch (IOException e) {

        }
        // 设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType(FileUtils.getContentType(objectName));
        final String key = fileProperties.getPrePath() + FileUtils.generatorName(objectName);

        cosClient.putObject(fileProperties.getBucketName(), key, inputStream, objectMetadata);
        return key;
    }

    @Override
    public InputStream getFile(String objectName) {
        COSObject object = null;
        try {
            object = cosClient.getObject(fileProperties.getBucketName(), objectName);
            return object.getObjectContent();
        } catch (CosClientException e) {
            throw new RuntimeException("文件不存在：" + objectName);
        }
    }

    @Override
    public void deleteFile(String objectName) {
        cosClient.deleteObject(fileProperties.getBucketName(), objectName);
    }
}
