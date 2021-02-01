package com.jenkin.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:45
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data

public class Response<T> implements Serializable {

    private String code;
    private  String msg;
    private T data;

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(String code, String msg, T t) {
        this.code = code;
        this.msg = msg;
        this.data = t;
    }

    public Response() {
    }

    public static  <T> Response<T> ok(){
        return new Response<>("200","请求成功");
    }
    public static <T> Response<T> ok(T t){
        return new Response<T>("200","请求成功",t);
    }
    public static  <T> Response <T> error(){
        return new Response<T>("500","请求失败");
    }
    public static <T> Response<T> error(String msg){
        return new Response<T>("500",msg);
    }
    public static <T> Response<T> error(String code,String msg){
        return new Response<T>(code,msg);
    }
}
