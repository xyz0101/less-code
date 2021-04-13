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
public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    Logger logger = LogManager.getLogger(ServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        HttpMethod method = msg.method();
        ByteBuf content = msg.content();
        String host = msg.headers().get("host");
        Attribute<byte[]> attr = ctx.channel().attr(NettyConst.REQUEST_INFO_ATTR);
        byte[] bytes = attr.getAndSet(null);

         logger.info("准备代理请求：{}", new String(bytes) );
        if("CONNECT".equals(method.name())){
            ctx.writeAndFlush("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
        }
        String key = UUID.randomUUID().toString();
        Object o = new Object();
        NettyConst.LOCK_MAP.put(key, o);
        synchronized (o){
            ChannelHandlerContext proxyChannel = new ProxyClient(host).getProxyChannel(ctx);
            proxyChannel.channel().attr(NettyConst.RESPONSE_WAIT_KEY_ATTR).set(key);
            proxyChannel.writeAndFlush(Unpooled.wrappedBuffer(bytes));
            o.wait();


        }

    }




}
