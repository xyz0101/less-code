package com.jenkin.proxy.server.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/10 19:46
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ProxySocketChanneFactory {
    private static final ConcurrentHashMap<String,ProxySocketChanelHander> PROXY_SOCKET_CHANEL_HANDER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    public static ProxySocketChanelHander getInstance(String host, int port, boolean isSSL, SocketChannel socketChannel, ByteBuffer byteBuffer){

        String key = host+":"+port;
        port=port==-1?(isSSL?443:80):port;
        ProxySocketChanelHander proxySocketChanelHander = PROXY_SOCKET_CHANEL_HANDER_CONCURRENT_HASH_MAP.get(key);
        if (proxySocketChanelHander==null) {
            proxySocketChanelHander = new ProxySocketChanelHander(host, port, socketChannel,isSSL,byteBuffer);
            PROXY_SOCKET_CHANEL_HANDER_CONCURRENT_HASH_MAP.put(key,proxySocketChanelHander);
        }
        return proxySocketChanelHander;
    }


    public static void removeInstance(String key){
        PROXY_SOCKET_CHANEL_HANDER_CONCURRENT_HASH_MAP.remove(key);
    }

}
