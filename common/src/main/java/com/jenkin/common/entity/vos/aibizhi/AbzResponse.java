/**
  * Copyright 2020 bejson.com 
  */
package com.jenkin.common.entity.vos.aibizhi;

/**
 * Auto-generated: 2020-12-28 16:13:6
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class AbzResponse<T> {

    private String msg;
    private Res<T> res;
    private int code;
    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setRes(Res<T> res) {
         this.res = res;
     }
     public Res<T>getRes() {
         return res;
     }

    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

}
