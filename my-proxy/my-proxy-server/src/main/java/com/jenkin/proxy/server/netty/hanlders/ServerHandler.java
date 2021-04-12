package com.jenkin.proxy.server.netty.hanlders;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/12 21:04
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    Logger logger = LogManager.getLogger(ServerHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        String s = buf.toString(CharsetUtil.UTF_8);
        logger.info("收到客户端发送的回的消息：{}", s);
        buf = Unpooled.wrappedBuffer(("返回给客户端："+s).getBytes());
         ctx.writeAndFlush(buf);
    }
}
