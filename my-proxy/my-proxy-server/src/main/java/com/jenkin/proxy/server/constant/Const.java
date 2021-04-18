package com.jenkin.proxy.server.constant;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/6 20:50
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Const {
    public static final int CORE_SIZE = Runtime.getRuntime().availableProcessors()*5;
    public static final int MAX_SIZE = Runtime.getRuntime().availableProcessors()*100;
    public static final int QUEUE_SIZE = 1000;
    public static final int ALIVE_TIME = 10;
    public static final int PROXY_CLIENT_QUEUE_SIZE = 1000;

    public static final int DEFAULT_BUFFER_SIZE = 8096;

}
