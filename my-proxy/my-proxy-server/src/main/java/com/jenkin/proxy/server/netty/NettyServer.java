package com.jenkin.proxy.server.netty;

import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.netty.hanlders.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/11 20:43
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Slf4j
public class NettyServer {


    public static void main(String[] args) {

        new NettyServer().startNettyServer(NettyConst.SERVER_PORT);

    }


    public void startNettyServer(int port){
        NioEventLoopGroup connectors = new NioEventLoopGroup();

        NioEventLoopGroup handlers = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(connectors,handlers)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,NettyConst.CONNECT_TIME_OUT*2)
                .option(ChannelOption.SO_BACKLOG,NettyConst.BACK_LOG_SIZE)
                .childOption(NioChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //http请求解码
                        socketChannel.pipeline().addLast(NettyConst.HTTP_REQUEST_DECODER, new HttpRequestDecoder());
                        //http响应编码
                        socketChannel.pipeline().addLast(NettyConst.HTTP_RESPONSE_ENCODER, new HttpResponseEncoder());
                        //http请求体的解码（post）
                        socketChannel.pipeline().addLast(NettyConst.HTTP_OBJECT_AGGERATOR,new HttpObjectAggregator(NettyConst.AGGREGATR_MAX_LENGTH));
                        //自定义处理器
                        socketChannel.pipeline().addLast(NettyConst.MY_SERVER_HANDLER,new ServerHandler());
                    }
                });
        try {
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            log.info("Netty代理服务启动成功，端口：{}",port);
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            connectors.shutdownGracefully();
            handlers.shutdownGracefully();
        }

    }



}
