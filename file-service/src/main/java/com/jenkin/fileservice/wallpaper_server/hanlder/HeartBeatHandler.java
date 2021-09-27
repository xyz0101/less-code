package com.jenkin.fileservice.wallpaper_server.hanlder;

import com.alibaba.fastjson.JSON;
import com.jenkin.fileservice.wallpaper_server.server.Server;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author ：jenkin
 * @date ：Created at 2021/9/26 16:33
 * @description：心跳检测
 * @modified By：
 * @version: 1.0
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("心跳检测服务激活");

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes((ByteBuf) msg);
        String s = new String(bytes, StandardCharsets.UTF_8);

        Server.Option req = JSON.parseObject(s, Server.Option.class);
        log.info("收到消息：{}",req);
        if ("heart".equalsIgnoreCase(req.getOperaType())) {
            log.info("接收到心跳消息：{}", s);
            Server.Option option = new Server.Option();
            option.setOperaType("heart");
            option.setUserCode(req.getUserCode());
            ByteBuf byteBuf = Unpooled.wrappedBuffer(JSON.toJSONString(option).getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush(byteBuf);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
