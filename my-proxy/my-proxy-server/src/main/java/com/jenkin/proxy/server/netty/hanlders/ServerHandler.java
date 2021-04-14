package com.jenkin.proxy.server.netty.hanlders;

import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.netty.proxyclient.ProxyClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
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
        if (msg instanceof FullHttpRequest) {
            request = (FullHttpRequest) msg;
            HttpMethod method = request.method();
            String host = request.headers().get("host");
            if("CONNECT".equals(method.name())){
                ctx.writeAndFlush("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
            }
            Channel proxyChannel = new ProxyClient(host).getProxyChannel(ctx);
            logger.info("发送给代理客户端");
            proxyChannel.writeAndFlush(msg).addListener(future -> {
                if(future.isSuccess())
                    logger.info("发送给代理客户端数据成功.");
                else
                    logger.info("发送给代理客户端数据失败.e:{}",future.cause().getMessage(),future.cause());
            });

        }



    }




}
