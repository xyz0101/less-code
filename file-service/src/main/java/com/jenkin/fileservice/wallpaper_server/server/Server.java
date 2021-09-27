package com.jenkin.fileservice.wallpaper_server.server;

import com.jenkin.fileservice.wallpaper_server.hanlder.HeartBeatHandler;
import com.jenkin.fileservice.wallpaper_server.hanlder.LoginHandler;
import com.jenkin.fileservice.wallpaper_server.hanlder.WallpaperHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * @author ：jenkin
 * @date ：Created at 2021/9/26 10:49
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Slf4j
public class Server {
    /**
     * 与目标主机连接超时的时间 ms
     */
    public static final Integer CONNECT_TIME_OUT = 5000*10;
    /**
     * 半连接队列的大小
     */
    public static final Integer BACK_LOG_SIZE = 2048;

    public static void main(String[] args) {
        new Server().start(23456);
    }

    public static final String DEMITER="````";


    public void start(int port){
        NioEventLoopGroup connectors = new NioEventLoopGroup();

        NioEventLoopGroup handlers = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(connectors,handlers)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,CONNECT_TIME_OUT*2)
                .option(ChannelOption.SO_BACKLOG,BACK_LOG_SIZE)
                .childOption(NioChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 创建分隔符缓冲对象$_作为分割符
                        ByteBuf byteBuf = Unpooled.copiedBuffer(DEMITER.getBytes());
                        /**
                         * 第一个参数：单条消息的最大长度，当达到最大长度仍然找不到分隔符抛异常，防止由于异常码流缺失分隔符号导致的内存溢出
                         * 第二个参数：分隔符缓冲对象
                         */
                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*10,byteBuf));
                        //自定义处理器
                        socketChannel.pipeline().addLast("login",new LoginHandler());
                        socketChannel.pipeline().addLast("heartBeat",new HeartBeatHandler());
                        socketChannel.pipeline().addLast("wallpaper",new WallpaperHandler());
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
    @Data
    public static class Option{

        private String operaType;

        private String operateData;

        private String userCode;
    }
}
