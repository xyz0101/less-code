package com.jenkin.proxy.server.netty.proxyclient;

import com.jenkin.proxy.server.entities.NettyProxyChannels;
import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.netty.proxyclient.handlers.ProxyClientResponseHandler;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author jenkin
 * @className ProxyClient
 * @description TODO
 * @date 2021/4/13 11:31
 */
@Slf4j
public class ProxyClient {

    private String host;

    private int port;

    private Boolean isHttps;

    public ProxyClient(String host,int port,Boolean isHttps){
            this.host=host;
            this.port=port;
            this.isHttps=isHttps;
    }
    public ProxyClient(String address,Boolean isHttps ){
        String[] split = address.split(":");
        if (split.length>1) {
            this.host=split[0];
            this.port=Integer.parseInt(split[1]);
        }else{
            this.host=address;
            this.port=isHttps!=null&&isHttps?443:80;
        }
        this.isHttps=isHttps;

    }

    public Channel getProxyChannel(ChannelHandlerContext ctx) throws InterruptedException {
        NettyProxyChannels serverAndProxyChannel = getServerAndProxyChannel(ctx);
        if (serverAndProxyChannel==null){
            throw new RuntimeException("代理连接失败，主机 :"+host+":"+port);
        }
        return  serverAndProxyChannel.getProxyChannel();
    }

    /**
     * 获取代理和客户端的channel对
     * @param ctx
     * @return
     * @throws InterruptedException
     */
    public NettyProxyChannels getServerAndProxyChannel(ChannelHandlerContext ctx) throws InterruptedException {
        String key = ctx.channel().attr(NettyConst.PROXY_CHANNEL_KEY_ATTR).get();
        NettyConst.LOCK_MAP.put(key,new Object());
        NettyProxyChannels nettyProxyChannels = NettyConst.CHANNEL_MAP.get(key);
        if(nettyProxyChannels==null||nettyProxyChannels.getProxyChannel()==null||!nettyProxyChannels.getProxyChannel().isActive()){
            Object o = NettyConst.LOCK_MAP.get(key);
            synchronized (o){
                log.info("准备代理客户端连接");
                NettyConst.PROXY_CLIENT_EXECUTORS.execute(()->startClient(key));

                log.info("等待代理客户端连接:{} ,已使用线程数量：{}",host,NettyConst.PROXY_CLIENT_EXECUTORS.getActiveCount());
                o.wait();
                NettyProxyChannels proxyChannels = NettyConst.CHANNEL_MAP.get(key);
                if(proxyChannels!=null) {
                    proxyChannels.setServerChannel(ctx);

                    return proxyChannels;
                }
                return null;
            }
        }else{
            return nettyProxyChannels;
        }
    }

    private void startClient(String key){
        log.info("开始连接代理 host:{}，port：{}",host,port);
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        if (isHttps==null||!isHttps) {
                            ch.pipeline().addLast(new HttpClientCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(1*1024*1024));
                        }
                        ch.pipeline().addLast(new ProxyClientResponseHandler());

                    }
                });

        try {
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress(this.host, this.port)).addListener(future -> {
                if (!future.isSuccess()){
                    Object o = NettyConst.LOCK_MAP.get(key);
                    synchronized (o){
                        o.notify();
                        log.error("代理主机连接失败，e:{}", future.cause().getMessage(), future.cause());

                    }
                }else{
                    log.info("代理主机连接成功");
                }
            }).sync();
            Channel channel = sync.channel();

            log.info("代理连接激活,channelId {}",key);
            NettyProxyChannels nettyProxyChannels = new NettyProxyChannels();
            nettyProxyChannels.setProxyChannel(channel);
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
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }


    }







}
