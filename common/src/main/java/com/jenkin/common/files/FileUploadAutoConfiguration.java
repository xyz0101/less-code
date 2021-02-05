package com.jenkin.common.files;

import com.aliyun.oss.OSS;
import com.jenkin.common.files.fileservice.FileService;
import com.jenkin.common.files.fileservice.impl.AliyunOssUpload;
import com.jenkin.common.files.fileservice.impl.MinioUploadService;
import com.jenkin.common.files.fileservice.impl.QcloudCosUpload;
import com.jenkin.common.files.properties.FileProperties;
import com.qcloud.cos.COSClient;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
/**
 * @author jenkin
 * @date 2020年12月15日16:09:42
 */
@Configuration
@Slf4j
public class FileUploadAutoConfiguration {

    @Autowired
    private FileProperties fileProperties;

    @Bean
    @ConditionalOnProperty(prefix = "file-store", name = "type", havingValue = "oss")
    @ConditionalOnBean(OSS.class)
    public FileService aliyunOssUpload(OSS ossClient){
        log.info("初始化oss service ");
        final AliyunOssUpload ossUpload = new AliyunOssUpload();
        ossUpload.setFileProperties(fileProperties);
        ossUpload.setOssClient(ossClient);
        return ossUpload;
    }

    @Bean
    @ConditionalOnProperty(prefix = "file-store", name = "type", havingValue = "minio")
    @ConditionalOnBean(MinioClient.class)
    @Primary
    public FileService minioUpload(MinioClient minioClient){
        log.info("初始化 minio service ");
        final MinioUploadService ossUpload = new MinioUploadService();
        ossUpload.setFileProperties(fileProperties);
        ossUpload.setMinioClient(minioClient);
        return ossUpload;
    }

    @Bean
    @ConditionalOnProperty(prefix = "file-store", name = "type", havingValue = "cos")
    @ConditionalOnBean(COSClient.class)
    public FileService qcloudCosUpload(COSClient cosClient){
        log.info("初始化cos service ");
        final QcloudCosUpload ossUpload = new QcloudCosUpload();
        ossUpload.setFileProperties(fileProperties);
        ossUpload.setCosClient(cosClient);
        return ossUpload;
    }

}
