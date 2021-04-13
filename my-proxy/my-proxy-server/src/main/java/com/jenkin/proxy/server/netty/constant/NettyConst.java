package com.jenkin.proxy.server.netty.constant;

import com.jenkin.proxy.server.constant.Const;
import com.jenkin.proxy.server.entities.NettyProxyChannels;
import com.jenkin.proxy.server.netty.proxyclient.ProxyClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.concurrent.*;

/**
 * @author jenkin
 * @className Const
 * @description TODO
 * @date 2021/4/13 14:23
 */
public class NettyConst {

    public static final ConcurrentHashMap<String, NettyProxyChannels> CHANNEL_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Object> LOCK_MAP = new ConcurrentHashMap<>();


    public static final AttributeKey<byte[]> REQUEST_INFO_ATTR = AttributeKey.newInstance("request");
    public static final AttributeKey<String> RESPONSE_WAIT_KEY_ATTR = AttributeKey.newInstance("wait-response");
    public static final AttributeKey<byte[]> RESPONSE_INFO_ATTR = AttributeKey.newInstance("response");




    public static final ExecutorService PROXY_CLIENT_EXECUTORS = new ThreadPoolExecutor(
            Const.CORE_SIZE, Const.MAX_SIZE,
            Const.ALIVE_TIME, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(Const.QUEUE_SIZE),
            new ThreadPoolExecutor.AbortPolicy()
    );

}
