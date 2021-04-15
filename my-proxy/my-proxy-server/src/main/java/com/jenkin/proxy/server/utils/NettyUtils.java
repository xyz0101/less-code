package com.jenkin.proxy.server.utils;

import com.jenkin.proxy.server.entities.NettyProxyChannels;
import com.jenkin.proxy.server.netty.constant.NettyConst;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/13 21:23
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Slf4j
public class NettyUtils {

    /**
     * 关闭和移除通道
     * @param ctx
     */
    public static void closeAndRemoveChannel(ChannelHandlerContext ctx){
        String key = ctx.channel().attr(NettyConst.PROXY_CHANNEL_KEY_ATTR).get();
        if (key==null){
            log.warn("key 为空 ");
            return;
        }
        NettyProxyChannels nettyProxyChannels = NettyConst.CHANNEL_MAP.get(key);
        nettyProxyChannels.closeAll();
        NettyConst.CHANNEL_MAP.remove(key);
    }

    public static void notifyKey(String key) {
        Object o = NettyConst.LOCK_MAP.get(key);
        if (o!=null) {
            synchronized (o) {
                o.notify();
            }
        }else{
            log.error("当前key：{} 的锁为空",key);
        }
    }
}
