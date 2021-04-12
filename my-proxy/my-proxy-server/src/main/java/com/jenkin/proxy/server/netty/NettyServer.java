package com.jenkin.proxy.server.netty;

import com.jenkin.proxy.server.netty.hanlders.ServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/11 20:43
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class NettyServer {


    public static void main(String[] args) {
        NioEventLoopGroup connectors = new NioEventLoopGroup();
        NioEventLoopGroup handlers = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(connectors,handlers)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("server-handler",new ServerHandler());
                    }
                });
        try {
            ChannelFuture sync = serverBootstrap.bind(15557).sync();

            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            connectors.shutdownGracefully();
            handlers.shutdownGracefully();
        }


    }




}
