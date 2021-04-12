package com.jenkin.proxy.server.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/12 20:56
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ServerChannelHanlder implements ChannelHandler {
    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {

    }

    /**
     * @param channelHandlerContext
     * @param throwable
     * @deprecated
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

    }
}
