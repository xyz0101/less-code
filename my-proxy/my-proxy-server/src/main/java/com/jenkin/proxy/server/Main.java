package com.jenkin.proxy.server;

import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.netty.proxyclient.ProxyClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/28 21:24
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Main {
    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) throws InterruptedException {
        String key = "tencent.jenkin.tech:8000";
        NettyConst.LOCK_MAP.put(key,new Object());
        Object o = NettyConst.LOCK_MAP.get(key);



    }
}

