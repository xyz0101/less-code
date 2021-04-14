package com.jenkin.proxy.server.netty.proxyclient.handlers;

import com.jenkin.proxy.server.entities.NettyProxyChannels;
import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;


/**
 * @author jenkin
 * @className ProxyClientHandler
 * @description TODO
 * @date 2021/4/13 13:50
 */
@Slf4j
public class ProxyClientResponseHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读事件
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                String channelKey =  ctx.channel().attr(NettyConst.PROXY_CHANNEL_KEY_ATTR).get();
                log.info("代理客户端server CHANEL ID {}",channelKey);
                if (channelKey!=null) {
                    NettyProxyChannels nettyProxyChannels = NettyConst.CHANNEL_MAP.get(channelKey);
                    log.info("收到整个响应，写给服务端:{}", msg);
                    nettyProxyChannels.getServerChannel().writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess())
                            log.info("向客户端写入数据成功.");
                        else
                            log.info("向客户端写入数据失败.e:{}", future.cause().getMessage(), future.cause());
                    });
                }

    }



    /**
     * 连接激活事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }


    /**
     * 异常捕获
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        NettyUtils.closeAndRemoveChannel(ctx);
        ctx.close();
        cause.printStackTrace();
    }

    /**
     * 连接关闭事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("代理连接关闭！");
        ctx.close();
//        NettyUtils.closeAndRemoveChannel(ctx);

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("代理取消注册！");
        super.channelUnregistered(ctx);
    }
}
