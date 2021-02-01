package com.jenkin.common.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author jenkin
 * @className DateUtils
 * @description TODO
 * @date 2021/2/1 16:06
 */
public class DateUtils {
    public static String parseTime(Date time) {
        if (time==null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }
    public static String parseTime(LocalDateTime time) {
        if (time==null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format( Date.from(time.atZone(ZoneId.systemDefault()).toInstant()));
    }
}
