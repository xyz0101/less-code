package com.jenkin.proxy.server.netty.hanlders;

import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.netty.proxyclient.ProxyClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.AttributeKey;
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
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("ByteBuf handler 激活");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.info("ByteBuf handler读取");
        if(msg instanceof ByteBuf) {
            ByteBuf duplicate = ((ByteBuf) msg).duplicate();
            duplicate.resetReaderIndex();
            int readableBytes = duplicate.readableBytes();
            byte[] bytes = new byte[readableBytes];
            int readerIndex = duplicate.readerIndex();
            duplicate.getBytes(readerIndex, bytes);
            ctx.channel().attr(NettyConst.REQUEST_INFO_ATTR).set(bytes);
        }else{
            logger.error("msg 类型不匹配");
        }
        ctx.fireChannelRead(msg);
//        FullHttpRequest fullHttpRequest = ctx.channel().attr(AttributeKey.<FullHttpRequest>valueOf(NettyConst.REQUEST_INFO_ATTR)).get();

//
//        ChannelHandlerContext proxyChannel = new ProxyClient(host).getProxyChannel();
//        proxyChannel.writeAndFlush(msg.content());
    }


}
