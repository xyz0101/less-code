package com.jenkin.common.files.fileservice.impl;

import com.jenkin.common.files.fileservice.FileService;
import com.jenkin.common.files.properties.FileProperties;
import com.jenkin.common.utils.FileUtils;
import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
/**
 * @author jenkin
 * @date 2020年12月15日16:09:42
 */
@Slf4j
@Data
public class MinioUploadService implements FileService {
    private FileProperties fileProperties;

    private MinioClient minioClient;


    @Override
    public String uploadFile(MultipartFile multipartFile) {
        Assert.notNull(multipartFile, "文件不能为空");
        try (InputStream inputStream = multipartFile.getInputStream()) {
            final String originalFilename = multipartFile.getOriginalFilename();
            String objectName = FileUtils.generatorName(originalFilename);
            minioClient.putObject(fileProperties.getBucketName(), objectName, inputStream, FileUtils.getContentType(originalFilename));
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败:" + e.getMessage());
        }
    }

    @Override
    public String uploadFile(File file) {
        try {
            final String objectName = FileUtils.generatorName(file.getName());
            log.info("开始上传文件{}",objectName);
            minioClient.putObject(fileProperties.getBucketName(), objectName, file.getPath());
            return objectName;
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败");
        }
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName) {
        try {
            String objectName = FileUtils.generatorName(fileName);
            minioClient.putObject(fileProperties.getBucketName(), objectName, inputStream, FileUtils.getContentType(fileName));
            return objectName;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException("文件上传失败");
        }
    }

    @Override
    public InputStream getFile(String objectName) {
        try {
            return minioClient.getObject(fileProperties.getBucketName(), objectName.replace(fileProperties.getBucketName(), ""));
        } catch (Exception e) {
            throw new RuntimeException("获取文件失败: "+objectName);
        }
    }


    @Override
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(fileProperties.getBucketName(), objectName.replace(fileProperties.getBucketName(), ""));
        } catch (Exception e) {
            throw new RuntimeException("获取上传失败");
        }
    }

}
