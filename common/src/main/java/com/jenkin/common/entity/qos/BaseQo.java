package com.jenkin.common.entity.qos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jenkin
 * @className BaseQO
 * @description
 * @date 2020/6/23 9:54
 */
@Data
@ApiModel("分页查询基类")
public class BaseQo<T> {
    @ApiModelProperty("查询的分页页码")
    private Integer page;
    @ApiModelProperty("分页大小")
    private Integer pageSize;
    @ApiModelProperty("排序条件")
    private List<Sort> sorts;
    @ApiModelProperty("查询参数")
    private T data;

    public List<Sort> getSorts(){
        sorts=sorts==null?new ArrayList<>():sorts;
        return sorts;
    }

}
