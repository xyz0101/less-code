/**
  * Copyright 2020 bejson.com 
  */
package com.jenkin.common.entity.vos.aibizhi;
import java.util.List;

/**
 * Auto-generated: 2020-12-28 16:13:6
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Res<T> {

    private List<T> wallpaper;
    private List<T> category;



    public List<T> getCategory() {
        return category;
    }

    public void setCategory(List<T> category) {
        this.category = category;
    }

    public List<T> getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(List<T> wallpaper) {
        this.wallpaper = wallpaper;
    }
}
