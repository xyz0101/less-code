package com.jenkin.proxy.server.other;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/5 23:17
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Test {
    public static void main(String[] args) throws IOException {
        //监听端口
        ServerSocket serverSocket = new ServerSocket(15556);
        for (; ; ) {
            new SocketHandle(serverSocket.accept()).start();
        }
    }
}
