package com.jenkin.proxy.server.netty.hanlders;

import com.jenkin.proxy.server.entities.NettyProxyChannels;
import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.netty.proxyclient.ProxyClient;
import com.jenkin.proxy.server.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/12 21:04
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ServerHandler extends ChannelInboundHandlerAdapter  {
    Logger logger = LogManager.getLogger(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request=null;
        Channel proxyChannel=null;
        if (msg instanceof FullHttpRequest) {
            request = (FullHttpRequest) msg;
            HttpMethod method = request.method();
            String host = request.headers().get("host");

            String key = ctx.channel().id().asLongText();
            ctx.channel().attr(NettyConst.PROXY_CHANNEL_KEY_ATTR).set(key);
            ctx.channel().attr(NettyConst.PROXY_CHANNEL_HOST_ATTR).set(host);
            if("CONNECT".equals(method.name())){
                ctx.channel().attr(NettyConst.CHANNEL_ISHTTPS_ATTR).set(true);

                proxyChannel=new ProxyClient(host,true ).getProxyChannel(ctx);
                proxyChannel.attr(NettyConst.PROXY_CHANNEL_KEY_ATTR).set(key);
                proxyChannel.attr(NettyConst.PROXY_CHANNEL_HOST_ATTR).set(host);
                logger.info("当前channel是https，删除解码和聚合的handler");
                ctx.pipeline().remove(NettyConst.HTTP_OBJECT_AGGERATOR);
                ctx.pipeline().remove(NettyConst.HTTP_REQUEST_DECODER);
                ctx.pipeline().remove(NettyConst.HTTP_RESPONSE_ENCODER);
                ctx.writeAndFlush(Unpooled.wrappedBuffer("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes()))
                        .addListener(future -> {
                    if(future.isSuccess())
                        logger.info("HTTPS CONNECT 回应成功.");
                    else
                        logger.info("HTTPS CONNECT 回应失败.e:{}",future.cause().getMessage(),future.cause());
                });
                ;
                return;

            }else{
                proxyChannel = new ProxyClient(host,ctx.channel().attr(NettyConst.CHANNEL_ISHTTPS_ATTR).get() ).getProxyChannel(ctx);
                proxyChannel.attr(NettyConst.PROXY_CHANNEL_KEY_ATTR).set(key);
                proxyChannel.attr(NettyConst.PROXY_CHANNEL_HOST_ATTR).set(host);
                logger.info("发送HTTP 消息给代理客户端");
                proxyChannel.writeAndFlush(msg).addListener(future -> {
                    if(future.isSuccess())
                        logger.info("发送给代理客户端数据成功.");
                    else
                        logger.info("发送给代理客户端数据失败.e:{}",future.cause().getMessage(),future.cause());
                });

            }

        }else{
            String host = ctx.channel().attr(NettyConst.PROXY_CHANNEL_HOST_ATTR).get();
            proxyChannel = new ProxyClient(host, true).getProxyChannel(ctx);
            logger.info("发送给HTTPS 消息代理客户端");
            proxyChannel.writeAndFlush(msg).addListener(future -> {
                if(future.isSuccess())
                    logger.info("发送给代理客户端数据成功.");
                else
                    logger.info("发送给代理客户端数据失败.e:{}",future.cause().getMessage(),future.cause());
            });


        }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server 异常");
//        NettyUtils.closeAndRemoveChannel(ctx);
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        NettyUtils.closeAndRemoveChannel(ctx);
        logger.warn("代理服务端连接关闭");
        ctx.close();
    }
}
