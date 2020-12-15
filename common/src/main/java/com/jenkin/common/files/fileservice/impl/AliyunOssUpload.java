package com.jenkin.common.files.fileservice.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.jenkin.common.files.fileservice.FileService;
import com.jenkin.common.files.properties.FileProperties;
import com.jenkin.common.utils.FileUtils;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author jenkin
 * @date 2020年12月15日16:09:42
 */
@Data
public class AliyunOssUpload implements FileService {

    private FileProperties fileProperties;
    private OSS ossClient;

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        final String s1 = FileUtils.generatorName(multipartFile.getOriginalFilename());
        try (InputStream inputStream = multipartFile.getInputStream()) {
            final ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(FileUtils.getContentType(multipartFile.getOriginalFilename()));
            ossClient.putObject(fileProperties.getBucketName(), s1, inputStream,objectMetadata);
            return s1;
        } catch (IOException e) {
            throw new RuntimeException("上传失败");
        }

    }

    @Override
    public String uploadFile(File file) {
        final String s1 = FileUtils.generatorName(file.getName());
        ossClient.putObject(fileProperties.getBucketName(), s1, file);
        return s1;
    }

    @Override
    public String uploadFile(InputStream inputStream, String objectName) {
        final PutObjectResult putObjectResult = ossClient.putObject(fileProperties.getBucketName(), objectName, inputStream);
        return null;
    }

    @Override
    public InputStream getFile(String objectName) {
        final OSSObject object = ossClient.getObject(fileProperties.getBucketName(), objectName);
        return object.getObjectContent();
    }

    @Override
    public void deleteFile(String objectName) {
        ossClient.deleteObject(fileProperties.getBucketName(), objectName);
    }
}
