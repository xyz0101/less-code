package com.jenkin.menuservice.entity.qos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jenkin
 * @className Sort
 * @description
 * @date 2020/6/23 14:02
 */
@Data
@ApiModel("排序")
@AllArgsConstructor
public class Sort {
    @ApiModelProperty("排序字段")
    private String sortField;
    @ApiModelProperty("排序值，升序 asc还是降序desc")
    private String sortValue;

}
