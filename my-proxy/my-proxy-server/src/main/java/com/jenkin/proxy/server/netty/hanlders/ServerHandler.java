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
import io.netty.util.AttributeMap;
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
         if (msg instanceof FullHttpRequest) {
             FullHttpRequest request = (FullHttpRequest) msg;
            String host = request.headers().get("host");
            if (host==null){
                ctx.writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.BAD_REQUEST));
                ctx.close();
                return;
            }
            String key = ctx.channel().id().asLongText();
            //把key和host 设置到channel属性里面
            setKeyAndHost(key,host,ctx.channel());
            //处理connect请求
            if(HttpMethod.CONNECT.name().equals(request.method().name())){
                if(!responseConnectRequest(key,host,ctx)){
                    return;
                }
                return;
            }else{
                //处理http请求
                writeHttpResponseToClient(key,host,ctx,msg);
            }
        }else{
             //处理https请求
            writeHttpsResponseToClient(ctx,msg);
        }



    }

    /**
     * 把https请求写入代理客户端
     * @param ctx
     * @param msg
     * @throws InterruptedException
     */
    private void writeHttpsResponseToClient(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        String host = ctx.channel().attr(NettyConst.PROXY_CHANNEL_HOST_ATTR).get();
        Channel proxyChannel = new ProxyClient(host, true).getProxyChannel(ctx);
        logger.info("发送给HTTPS 消息代理客户端");
        proxyChannel.writeAndFlush(msg).addListener(future -> {
            if(future.isSuccess())
                logger.info("发送给HTTPS 消息给代理客户端成功.");
            else
                logger.error("发送给HTTPS 消息给代理客户端失败.e:{}",future.cause().getMessage(),future.cause());
        });
    }

    /**
     * 把HTTP请求写入代理客户端
     * @param key
     * @param host
     * @param ctx
     * @param msg
     * @throws InterruptedException
     */
    private void writeHttpResponseToClient(String key, String host, ChannelHandlerContext ctx,Object msg) throws InterruptedException {
        Channel proxyChannel = new ProxyClient(host,ctx.channel().attr(NettyConst.CHANNEL_ISHTTPS_ATTR).get()).getProxyChannel(ctx);
        setKeyAndHost(key,host,proxyChannel);
        logger.info("发送HTTP 请求消息给代理客户端");
        proxyChannel.writeAndFlush(msg).addListener(future -> {
            if(future.isSuccess())
                logger.info("发送HTTP 请求消息给代理客户端成功.");
            else
                logger.error("发送HTTP 请求消息给代理客户端失败.e:{}",future.cause().getMessage(),future.cause());
        });
    }

    /**
     * Connect请求直接返回200
     * @param key
     * @param host
     * @param ctx
     * @throws InterruptedException
     */
    private boolean responseConnectRequest(String key,String host,ChannelHandlerContext ctx) throws InterruptedException {
        ctx.channel().attr(NettyConst.CHANNEL_ISHTTPS_ATTR).set(true);
        Channel proxyChannel=new ProxyClient(host,true ).getProxyChannel(ctx);
        setKeyAndHost(key,host,proxyChannel);
        removeHttpPipline(ctx);
        ChannelFuture sync = ctx.writeAndFlush(Unpooled.wrappedBuffer("HTTP/1.1 200 Connection Established\r\n\r\n".getBytes()))
                .addListener(future -> {
                    if (future.isSuccess())
                        logger.info("HTTPS CONNECT 回应成功.");
                    else
                        logger.error("HTTPS CONNECT 回应失败.e:{}", future.cause().getMessage(), future.cause());
                }).sync();
        return sync.isSuccess();
    }

    /**
     * https请求的情况下因为数据是加密了的，所以需要删除编码解码器和聚合器
     * @param ctx
     */
    private void removeHttpPipline(ChannelHandlerContext ctx) {
        logger.debug("当前channel是https，删除解码和聚合的handler");
        ctx.pipeline().remove(NettyConst.HTTP_OBJECT_AGGERATOR);
        ctx.pipeline().remove(NettyConst.HTTP_REQUEST_DECODER);
        ctx.pipeline().remove(NettyConst.HTTP_RESPONSE_ENCODER);
    }

    /**
     * 为channel设置当前客户端连接所属的主机和key
     * @param key
     * @param host
     * @param map
     */
    private void setKeyAndHost(String key,String host, AttributeMap map) {
        map.attr(NettyConst.PROXY_CHANNEL_KEY_ATTR).set(key);
        map.attr(NettyConst.PROXY_CHANNEL_HOST_ATTR).set(host);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.warn("服务端取消注册");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server 异常");
//        NettyUtils.closeAndRemoveChannel(ctx);
        cause.printStackTrace();
//        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //TODO 2021年4月18日16:11:54
        // 大量请求是阻塞的原因是因为这里每次都会关闭连接，而代理客户端的连接实际上还没关闭，导致线程堆积
        // 但是为什么会这样？还不知道原因，这里关闭客户端的连接暂时解决，但是失去了keepalive 属性
        NettyUtils.closeAndRemoveChannel(ctx);
        //为什么服务端会主动关闭与客户端的连接？？ TODO
        logger.warn("代理服务端连接关闭");
//        ctx.close();
    }
}
