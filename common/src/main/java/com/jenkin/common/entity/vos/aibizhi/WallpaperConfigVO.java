package com.jenkin.common.entity.vos.aibizhi;

import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2021/9/26 19:14
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
public class WallpaperConfigVO<T> {
    private Boolean on;
    private T data;
}
