package com.jenkin.proxy.server;

import com.jenkin.proxy.server.bio.BioServer;
import com.jenkin.proxy.server.netty.NettyServer;
import com.jenkin.proxy.server.netty.constant.NettyConst;
import com.jenkin.proxy.server.netty.proxyclient.ProxyClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/28 21:24
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Main {
    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {

        if (args.length>=2){
            String type = args[0];
            int port = Integer.parseInt(args[1]);
            if("bio".equalsIgnoreCase(type)){
                new BioServer().startServer(port);
                logger.info("bioServer 启动成功，端口：{}",port);
            }else if("netty".equalsIgnoreCase(type)){
                new NettyServer().startNettyServer(port);
            }
        }else{
            throw new RuntimeException("请输入参数 类型（bio/netty）和端口号,例如 netty 15557");
        }



    }
}

