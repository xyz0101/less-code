package com.jenkin.sometools.btsearch.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/25 20:54
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@ApiModel("磁力信息")
@Document(indexName = "bt_info")
public class BtInfoVO {
    @Id
    private String id;
    private String infohash;

    private String name;

    private long length;

    private List<FileInfo> files;

    @Data
    static class FileInfo{
        private List<String> path;
        private long length;
    }

}
