package com.jenkin.proxy.server.netty.constant;

import com.jenkin.proxy.server.constant.Const;
import com.jenkin.proxy.server.netty.proxyclient.ProxyClient;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.*;

/**
 * @author jenkin
 * @className Const
 * @description TODO
 * @date 2021/4/13 14:23
 */
public class NettyConst {

    public static final ConcurrentHashMap<String, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Object> LOCK_MAP = new ConcurrentHashMap<>();

    public static final ExecutorService PROXY_CLIENT_EXECUTORS = new ThreadPoolExecutor(
            Const.CORE_SIZE, Const.MAX_SIZE,
            Const.ALIVE_TIME, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(Const.QUEUE_SIZE),
            new ThreadPoolExecutor.AbortPolicy()
    );

}
