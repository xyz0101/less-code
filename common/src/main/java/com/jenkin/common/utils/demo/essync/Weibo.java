package com.jenkin.common.utils.demo.essync;

import lombok.Data;

import java.util.Date;
@Data
public class Weibo {

  private String id;
  private String userId;
  private String content;
  private String articleUrl;
  private String originalPictures;
  private String retweetPictures;
  private long original;
  private String videoUrl;
  private String publishPlace;
  private Date publishTime;
  private String publishTool;
  private long upNum;
  private long retweetNum;
  private long commentNum;


}
