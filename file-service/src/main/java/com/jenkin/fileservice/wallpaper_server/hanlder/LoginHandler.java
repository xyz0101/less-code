package com.jenkin.fileservice.wallpaper_server.hanlder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jenkin.common.entity.dtos.file.WallpaperConfigDto;
import com.jenkin.common.utils.ApplicationContextProvider;
import com.jenkin.fileservice.service.aibizhi.WallpaperConfigService;
import com.jenkin.fileservice.wallpaper_server.WallpaperServerHelper;
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
 * @description：登录
 * @modified By：
 * @version: 1.0
 */
@Slf4j
public class LoginHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes((ByteBuf) msg);
        String str = new String(bytes, StandardCharsets.UTF_8);
        Server.Option req = JSON.parseObject(str, Server.Option.class);
        log.info("登录收到消息：{}",req);
        if ("login".equalsIgnoreCase(req.getOperaType())) {
            log.info("接收到登录消息：{}", str);
            Server.Option option = new Server.Option();
            option.setOperaType("loginSuccess");
            option.setUserCode(req.getUserCode());
            String password = option.getOperateData();
            String userCode = option.getUserCode();
            WallpaperServerHelper.addContext(userCode,ctx);
            WallpaperConfigService configService = ApplicationContextProvider.getBean(WallpaperConfigService.class);
            WallpaperConfigDto configByUser = configService.getConfigByUserWithAuth(userCode, password);
            if (configByUser==null){
                option.setOperaType("loginFailed");
            }else{
                log.info("获取到配置：{}", JSONObject.toJSONString(configByUser));
                option.setOperateData(configByUser.getStrategyValue());
            }
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
