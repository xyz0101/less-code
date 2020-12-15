package com.jenkin.common.files.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @author jenkin
 * @date 2020年12月15日16:09:42
 */
@ConfigurationProperties(prefix = "file-store")
@Component
@Data
public class FileProperties {

    /**
     *
     */
    private Boolean enabled;

    /**
     * 类型：oos、cos、minio、qiniu、fastdfs
     */
    private String type;

    private String endpoint;

    private String bucketName;

    private String accessKey;

    private String secretKey;
    /**
     * 访问路径
     */
    private String preUrl;
    /**
     * 路径前缀
     */
    private String prePath;

}
