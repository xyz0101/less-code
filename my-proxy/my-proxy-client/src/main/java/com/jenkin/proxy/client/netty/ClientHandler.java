package com.jenkin.proxy.client.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author ：jenkin
 * @date ：Created at 2021/4/12 21:15
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LogManager.getLogger(ClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端激活，开始连接");

        String msg = "你好 netty";
        ByteBuf byteBuf= Unpooled.wrappedBuffer(msg.getBytes());
        ctx.writeAndFlush(byteBuf);
     }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        logger.info("收到服务端返回的消息：{}",buf.toString(CharsetUtil.UTF_8));
    }
}
