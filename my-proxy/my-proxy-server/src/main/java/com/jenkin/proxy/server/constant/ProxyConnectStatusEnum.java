package com.jenkin.proxy.server.constant;

/**
 * @author jenkin
 * @className ProxyConnectStatusEnum
 * @description TODO
 * @date 2021/4/16 10:45
 */
public enum ProxyConnectStatusEnum {

    /**
     * 连接中
     */
    CONNECTING(0,"连接中"),
    /**
     * 连接中
     */
    CONNECT_SUCCESS(1,"连接成功"),
    /**
     * 连接中
     */
    CONNECT_FAIL(-1,"连接失败")
    ;

    private int status;

    private String name;



    private ProxyConnectStatusEnum(int status,String name){
        this.name=name;
        this.status=status;
    }


}
