/**
  * Copyright 2021 bejson.com 
  */
package com.jenkin.common.entity.vos.pic_deal;
import java.util.List;

/**
 * Auto-generated: 2021-03-11 12:22:54
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PicDealRes {

    private long log_id;
    private String labelmap;
    private String scoremap;
    private String foreground;
    private int person_num;
    private List<Person_info> person_info;

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLabelmap(String labelmap) {
        this.labelmap = labelmap;
    }

    public String getLabelmap() {
        return labelmap;
    }

    public void setScoremap(String scoremap) {
        this.scoremap = scoremap;
    }

    public String getScoremap() {
        return scoremap;
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    public String getForeground() {
        return foreground;
    }

    public void setPerson_num(int person_num) {
        this.person_num = person_num;
    }

    public int getPerson_num() {
        return person_num;
    }

    public void setPerson_info(List<Person_info> person_info) {
        this.person_info = person_info;
    }

    public List<Person_info> getPerson_info() {
        return person_info;
    }
}
