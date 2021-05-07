package com.jenkin.sometools.btsearch.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
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
public class BtInfoVO extends LengthVO{
    @Id
    private String id;
    private String infohash;

    private String name;
    private Date recordTime;
    private List<FileInfo> files;

    private float score;





    @Data
    public static class FileInfo extends LengthVO{
        private List<String> path;

    }

}
