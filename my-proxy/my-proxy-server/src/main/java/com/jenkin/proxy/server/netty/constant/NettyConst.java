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



    public static final ThreadPoolExecutor PROXY_CLIENT_EXECUTORS = new ThreadPoolExecutor(
            Const.CORE_SIZE, Const.MAX_SIZE,
            Const.ALIVE_TIME, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(Const.QUEUE_SIZE),
            new ThreadPoolExecutor.AbortPolicy()
    );

}
