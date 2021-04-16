package com.jenkin.proxy.server.entities;

import cn.hutool.core.io.IoUtil;
import com.jenkin.proxy.server.constant.ProxyConnectStatusEnum;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/13 21:49
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@Slf4j
public class NettyProxyChannels {

    private Channel proxyChannel;
    private ChannelHandlerContext serverChannel;
    private ProxyConnectStatusEnum connectStatus;
    public void closeAll(){
        try {
            proxyChannel.close().sync();
            serverChannel.close();
            log.warn("服务端和代理连接关闭");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
