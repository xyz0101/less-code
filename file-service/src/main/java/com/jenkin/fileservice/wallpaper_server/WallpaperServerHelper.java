package com.jenkin.fileservice.wallpaper_server;

import com.alibaba.fastjson.JSON;
import com.jenkin.common.entity.vos.aibizhi.Wallpaper;
import com.jenkin.fileservice.wallpaper_server.server.Server;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：jenkin
 * @date ：Created at 2021/9/27 12:34
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Slf4j
public class WallpaperServerHelper {

    private static final ConcurrentHashMap<String, ChannelHandlerContext> CONTEXT_MAP = new ConcurrentHashMap<>();


    public static void addContext(String key,ChannelHandlerContext context){
        if (key!=null){
            CONTEXT_MAP.put(key,context);
        }
    }


    public static void removeContext(String key){
        CONTEXT_MAP.remove(key);
    }
    public static ChannelHandlerContext getContext(String key){
        return CONTEXT_MAP.get(key);
    }

    /**
     * 发送更新配置的信息
     * @param userCode
     * @param config
     */
    public static boolean sendUpdateConfigMsg(String userCode,String config){
        ChannelHandlerContext context = getContext(userCode);
        if (context==null){
            return false;
        }
        Server.Option option = new Server.Option();
        option.setOperaType("changeStrategy");
        option.setOperateData(config);
        option.setUserCode(userCode);
        context.writeAndFlush(getBytebuf(option));
        log.info("更改策略通知发送");
        return true;
    }
    /**
     * 发送更新配置的信息
     * @param userCode
     * @param wallpaper
     */
    public static boolean sendChangeWallpaperMsg(String userCode, Wallpaper wallpaper){
        ChannelHandlerContext context = getContext(userCode);
        if (context==null){
            return false;
        }
        Server.Option option = new Server.Option();
        option.setOperaType("changeWallpaper");
        option.setOperateData(JSON.toJSONString(wallpaper));
        option.setUserCode(userCode);
        context.writeAndFlush(getBytebuf(option));
        return true;
    }



    public static ByteBuf getBytebuf(Object o){
       return Unpooled.wrappedBuffer(JSON.toJSONString(o).getBytes(StandardCharsets.UTF_8));
    }

}
