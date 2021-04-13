package com.jenkin.proxy.server.entities;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/13 21:49
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
public class NettyProxyChannels {

    private ChannelHandlerContext proxyChannel;
    private ChannelHandlerContext serverChannel;

}
