package com.jenkin.sometools.btsearch.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author jenkin
 * @className ShowableBtInfoVO
 * @description TODO
 * @date 2021/4/26 10:42
 */
@Data
public class ShowableBtInfoVO {
    @Id
    private String id;
    private String infohash;

    private String name;

    private long length;

    private List<BtInfoVO.FileInfo> files;

    @Data
    static class FileInfo{
        private List<String> path;
        private long length;
    }
}
