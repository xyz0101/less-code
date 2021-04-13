package com.jenkin.proxy.server.netty.proxyclient.handlers;

import com.jenkin.proxy.server.entities.NettyProxyChannels;
import com.jenkin.proxy.server.netty.constant.NettyConst;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;


/**
 * @author jenkin
 * @className ProxyClientHandler
 * @description TODO
 * @date 2021/4/13 13:50
 */
@Slf4j
public class ProxyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("代理客户端读取完成");
        super.channelReadComplete(ctx);
    }

    /**
     * 连接激活事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String key =socketAddress.getHostString()+":"+socketAddress.getPort();
        log.info("代理连接激活,地址{}",key);
        NettyProxyChannels nettyProxyChannels = new NettyProxyChannels();
        nettyProxyChannels.setProxyChannel(ctx);
        NettyConst.CHANNEL_MAP.put(key,nettyProxyChannels);
        Object o = NettyConst.LOCK_MAP.get(key);
        if (o!=null){
            synchronized (o){
                o.notify();
                log.info("唤醒代理启动锁");
            }

        }else{
            log.error("锁为空");
        }
    }


    /**
     * 异常捕获
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    /**
     * 连接关闭事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("代理连接关闭！");
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String key =socketAddress.getHostString()+":"+socketAddress.getPort();
        NettyProxyChannels nettyProxyChannels = NettyConst.CHANNEL_MAP.get(key);
        if (nettyProxyChannels!=null&&nettyProxyChannels.getServerChannel()!=null){
            nettyProxyChannels.getServerChannel().close();
        }
        NettyConst.CHANNEL_MAP.remove(key);
        super.channelInactive(ctx);
    }
}
