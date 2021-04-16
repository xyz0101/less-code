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
    /**
     * 保存代理服务channel和代理连接channel的关系
     */
    public static final ConcurrentHashMap<String, NettyProxyChannels> CHANNEL_MAP = new ConcurrentHashMap<>();
    /**
     * 锁集合
     */
    public static final ConcurrentHashMap<String, Object> LOCK_MAP = new ConcurrentHashMap<>();


    /**
     * 判断当前channel是不是https
     */
    public static final AttributeKey<Boolean> CHANNEL_ISHTTPS_ATTR = AttributeKey.newInstance("isHttps");

    /**
     * 保存当前channel的代理channel的key
     */
    public static final AttributeKey<String> PROXY_CHANNEL_KEY_ATTR = AttributeKey.newInstance("proxy_channel_key");
    /**
     * 保存当前channel的host
     */
    public static final AttributeKey<String> PROXY_CHANNEL_HOST_ATTR = AttributeKey.newInstance("proxy_channel_host");

    /**
     * HTTP 消息聚合器
     */
    public static final String HTTP_OBJECT_AGGERATOR = "full-http-handler";
    /**
     * 请求解码器
     */
    public static final String HTTP_REQUEST_DECODER = "HttpRequestDecoder";
    /**
     * 响应编码器
     */
    public static final String HTTP_RESPONSE_ENCODER = "HttpResponseEncoder";
    /**
     * 自定义的服务端处理器
     */
    public static final String MY_SERVER_HANDLER = "my-server-handler";


    /**
     * 连接目标主机的线程池
     */
    public static final ThreadPoolExecutor PROXY_CLIENT_EXECUTORS = new ThreadPoolExecutor(
            Const.CORE_SIZE, Const.MAX_SIZE,
            Const.ALIVE_TIME, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );
    /**
     * 端口号
     */
    public static final int SERVER_PORT = 15557;
    /**
     * http聚合器的最大报文长度
     */
    public static final int AGGREGATR_MAX_LENGTH = 100 * 1024 * 1024;
    /**
     * 与目标主机连接超时的时间 ms
     */
    public static final Integer CONNECT_TIME_OUT = 5000;
    /**
     * 半连接队列的大小
     */
    public static final Integer BACK_LOG_SIZE = 2048;
}
