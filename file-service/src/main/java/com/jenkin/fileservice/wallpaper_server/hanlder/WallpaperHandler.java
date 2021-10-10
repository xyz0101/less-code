package com.jenkin.fileservice.wallpaper_server.hanlder;

import com.alibaba.fastjson.JSON;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import com.jenkin.fileservice.wallpaper_server.WallpaperServerHelper;
import com.jenkin.fileservice.wallpaper_server.server.Server;
import com.jenkin.fileservice.wallpaper_server.strategy.BaseStrategy;
import com.jenkin.fileservice.wallpaper_server.strategy.WallpaperStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author ：jenkin
 * @date ：Created at 2021/9/26 18:31
 * @description：壁纸处理器
 * @modified By：
 * @version: 1.0
 */
@Slf4j
public class WallpaperHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("壁纸处理器服务激活");

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes((ByteBuf) msg);
        log.info("接收到壁纸处理消息：{}",new String(bytes, StandardCharsets.UTF_8));
        String s = new String(bytes, StandardCharsets.UTF_8);
        Server.Option req = JSON.parseObject(s, Server.Option.class);
        switch (req.getOperaType()){
            case "next":
                nextWallpaper(ctx,req);
                break;
            case "before":
               beforeWallpaper(ctx,req);
                break;
            case "stop":
                stopWallpaper(ctx,req);
                break;
            default:

                break;
        }
        ctx.fireChannelRead(msg);
    }

    private void stopWallpaper(ChannelHandlerContext ctx, Server.Option req) {

    }

    private void beforeWallpaper(ChannelHandlerContext ctx, Server.Option req) {

    }

    private void nextWallpaper(ChannelHandlerContext ctx, Server.Option req) {

        String operateData = req.getOperateData();
        log.info(operateData);
        BaseStrategy baseStrategy = JSON.parseObject(operateData, BaseStrategy.class);
        log.info("策略：",baseStrategy);
        if (WallpaperServerHelper.getContext(baseStrategy.getUserCode())==null){
            WallpaperServerHelper.addContext(baseStrategy.getUserCode(),ctx);
        }
        String strategyCode = baseStrategy.getStrategyCode();
        WallpaperStrategy instance = WallpaperStrategy.getInstance(strategyCode, operateData);
        Wallpaper wallpaper = instance.resolveWallpaper();
        String wallstr = JSON.toJSONString(wallpaper);
        req.setOperaType("changeWallpaper");
        req.setOperateData(wallstr);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(JSON.toJSONString(req).getBytes(StandardCharsets.UTF_8));
        log.info("切换下一个壁纸：{}",wallpaper==null?null:wallpaper.getImg());
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
