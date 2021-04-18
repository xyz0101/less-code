package com.jenkin.proxy.server.netty.proxyclient;

import com.jenkin.proxy.server.constant.ProxyConnectStatusEnum;
import com.jenkin.proxy.server.entities.NettyProxyChannels;
import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.netty.proxyclient.handlers.ProxyClientResponseHandler;
import com.jenkin.proxy.server.utils.NettyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
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
        if (nettyProxyChannels!=null&&
                nettyProxyChannels.getConnectStatus()==ProxyConnectStatusEnum.CONNECT_SUCCESS&&
                nettyProxyChannels.getProxyChannel().isActive()) {
            return nettyProxyChannels;
        }
            Object o = NettyConst.LOCK_MAP.get(key);
            synchronized (o){
                long t = System.currentTimeMillis();
                log.info("准备代理客户端连接, host: {}",host);
                if( checkIsConnecting(nettyProxyChannels)){
                    log.info("当前主机 {} 正在连接中.......",host);
                    o.wait();
                    log.info("当前主机 {} 连接成功.......",host);
                    return nettyProxyChannels;
                }
                nettyProxyChannels=new NettyProxyChannels();
                nettyProxyChannels.setConnectStatus(ProxyConnectStatusEnum.CONNECTING);
                nettyProxyChannels.setServerChannel(ctx);
                NettyConst.CHANNEL_MAP.put(key,nettyProxyChannels);
                NettyConst.PROXY_CLIENT_EXECUTORS.execute(()->startClient(key));
                log.info("等待代理客户端连接,核心线程数：{}，已使用线程数量：{}",NettyConst.PROXY_CLIENT_EXECUTORS.getCorePoolSize(),NettyConst.PROXY_CLIENT_EXECUTORS.getActiveCount());
                o.wait();
                NettyProxyChannels proxyChannels = NettyConst.CHANNEL_MAP.get(key);
                log.info("与主机：{} 建立连接成功。耗时：{}",host,System.currentTimeMillis()-t);
                if(proxyChannels!=null) {
                    proxyChannels.setServerChannel(ctx);
                    return proxyChannels;
                }
                return null;
            }

    }

    /**
     * 检查是否正在连接
     * @param nettyProxyChannels
     * @return
     */
    private boolean checkIsConnecting(NettyProxyChannels nettyProxyChannels) {
        if (nettyProxyChannels!=null&&nettyProxyChannels.getConnectStatus()==ProxyConnectStatusEnum.CONNECTING) {
             return true;
        }
        return false;
    }

    private void startClient(String key){
        log.info("开始连接代理 host:{}，port：{}",host,port);
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //初始化bootstrap
            initClientBootStrap(bootstrap,group);
            //连接目标主机
            connectToTargetHost(bootstrap,key);
        }finally {
            group.shutdownGracefully();
        }

    }

    /**
     * 代理客户端连接到目标主机
     * @param bootstrap
     * @param key
     */
    private void connectToTargetHost(Bootstrap bootstrap, String key) {
        try {
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress(this.host, this.port)).addListener(
                    future -> {
                        if (!future.isSuccess()){
                            log.error("代理主机连接失败，e:{}", future.cause().getMessage(), future.cause());
                            NettyProxyChannels nettyProxyChannels=NettyConst.CHANNEL_MAP.get(key);
                            if (nettyProxyChannels!=null&&nettyProxyChannels.getServerChannel()!=null){
                                nettyProxyChannels.getServerChannel()
                                        .writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                                                HttpResponseStatus.REQUEST_TIMEOUT));
                                log.info("响应超时");
                                nettyProxyChannels.getServerChannel().close();
                            }
                            NettyUtils.notifyKey(key);
                        }
                    }
            ).sync() ;
            Channel channel = sync.channel();
            NettyProxyChannels nettyProxyChannels=NettyConst.CHANNEL_MAP.get(key);
            nettyProxyChannels = nettyProxyChannels==null? new NettyProxyChannels():nettyProxyChannels;
            if (sync.isSuccess()) {
                log.info("代理连接成功，主机：{} ,channelId {}",host,key);
                nettyProxyChannels.setConnectStatus(ProxyConnectStatusEnum.CONNECT_SUCCESS);
            }else{
                nettyProxyChannels.setConnectStatus(ProxyConnectStatusEnum.CONNECT_FAIL);
                log.error("代理主机连接失败，e:{}", sync.cause().getMessage(), sync.cause());
            }
            nettyProxyChannels.setProxyChannel(channel);
            NettyConst.CHANNEL_MAP.put(key,nettyProxyChannels);
            NettyUtils.notifyKey(key);
            channel.closeFuture().sync();
            log.info("代理客户端channel关闭");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化代理客户端，在https的情况下不熏醋解码器 和聚合器
     * @param bootstrap
     * @param group
     */
    private void initClientBootStrap(Bootstrap bootstrap,EventLoopGroup group) {
        bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,NettyConst.CONNECT_TIME_OUT)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        if (isHttps==null||!isHttps) {
                            ch.pipeline().addLast(new HttpClientCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(NettyConst.AGGREGATR_MAX_LENGTH));
                        }
                        ch.pipeline().addLast(new ProxyClientResponseHandler());
                    }
                });
    }


}
