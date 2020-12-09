package com.jenkin.simpleshiro.entity;

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

    public static Response ok(){
        return new Response("200","请求成功");
    }
    public static <T> Response<T> ok(T t){
        return new Response<T>("200","请求成功",t);
    }
    public static Response error(){
        return new Response("500","请求失败");
    }
    public static Response error(String msg){
        return new Response("500",msg);
    }
}
