package com.jenkin.proxy.server.netty.hanlders;

import com.jenkin.proxy.server.netty.proxyclient.ProxyClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/12 21:04
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ServerProxyHandler extends ChannelInboundHandlerAdapter  {
    Logger logger = LogManager.getLogger(ServerProxyHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


//
//        ChannelHandlerContext proxyChannel = new ProxyClient(host).getProxyChannel();
//        proxyChannel.writeAndFlush(msg.content());
    }


}
