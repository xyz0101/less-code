package com.jenkin.proxy.server.netty.proxyclient;

import com.jenkin.proxy.server.entities.NettyProxyChannels;
import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.netty.proxyclient.handlers.ProxyClientHandler;
import com.jenkin.proxy.server.netty.proxyclient.handlers.ProxyClientResponseHandler;
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

    public ProxyClient(String host,int port){
            this.host=host;
            this.port=port;
    }
    public ProxyClient(String address ){
        String[] split = address.split(":");
        if (split.length>1) {
            this.host=split[0];
            this.port=Integer.parseInt(split[1]);
        }else{
            this.host=address;
            this.port=80;
        }

    }

    public ChannelHandlerContext getProxyChannel(ChannelHandlerContext ctx) throws InterruptedException {
        String key = this.host+":"+this.port;
        NettyConst.LOCK_MAP.put(key,new Object());
        NettyProxyChannels nettyProxyChannels = NettyConst.CHANNEL_MAP.get(key);
        if(nettyProxyChannels==null||nettyProxyChannels.getProxyChannel()==null){
            Object o = NettyConst.LOCK_MAP.get(key);
            synchronized (o){
                log.info("准备代理客户端连接");
                NettyConst.PROXY_CLIENT_EXECUTORS.execute(this::startClient);
                log.info("等待代理客户端连接");
                o.wait();
                NettyProxyChannels proxyChannels = NettyConst.CHANNEL_MAP.get(key);
                proxyChannels.setServerChannel(ctx);
                return proxyChannels.getProxyChannel();
            }
        }else{
            return nettyProxyChannels.getProxyChannel();
        }
    }

    private void startClient(){
        log.info("开始连接代理 host:{}，port：{}",host,port);
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY,true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ProxyClientHandler());


                        ch.pipeline().addLast(new HttpResponseDecoder());
                        ch.pipeline().addLast(new HttpRequestEncoder());
                        ch.pipeline().addLast(new HttpObjectAggregator(1*1024*1024));
                        ch.pipeline().addLast(new ProxyClientResponseHandler());

                    }
                });

        try {
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress(this.host, this.port)).sync();

            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }


    }







}
