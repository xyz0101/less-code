package com.jenkin.proxy.server.netty.proxyclient.handlers;

import com.jenkin.proxy.server.netty.constant.NettyConst;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
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
public class ProxyClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    /**
     * 读事件
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        log.info("代理客户端收到返回消息：{}",new String(msg.content().toString()));
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
        NettyConst.CHANNEL_MAP.put(key,ctx);
        Object o = NettyConst.LOCK_MAP.get(key);
        if (o!=null){
            synchronized (o){
                o.notify();
                log.info("唤醒代理启动锁");
            }

        }else{
            log.error("锁为空");
        }
        super.channelActive(ctx);
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
        super.channelInactive(ctx);
    }
}
