package com.jenkin.proxy.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.jenkin.proxy.server.constant.Const.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/6 20:40
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class NioServer {

    static ByteBuffer readBuffer =  ByteBuffer.allocate(10);
    static ByteBuffer writeBuffer =  ByteBuffer.allocate(10);
    private static Selector selector;
    private  static final ExecutorService CONNECT_SERVICE = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //设置为异步非阻塞
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(15556));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            chooseSelect(selector,serverSocketChannel);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void chooseSelect(Selector selector, ServerSocketChannel serverSocketChannel)  {
        while(true){
            try {

                int select = selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (!selectionKey.isValid()) {
                        continue;
                    }
                    if (selectionKey.isAcceptable()) {
                        onAccept(selectionKey);
                    }
                    if (selectionKey.isReadable()) {
                        onRead(selectionKey);
                    }
                    if (selectionKey.isWritable()) {
                        onWrite(selectionKey);
                    }
                    iterator.remove();

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    private static void onWrite(SelectionKey  selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        String write = "write:";
        try {
            writeBuffer.clear();
            writeBuffer.put(write.getBytes());
            writeBuffer.flip();//反转，由写变为读
            channel.write(writeBuffer);
            //注册读操作 下一次进行读
            channel.register(selector,SelectionKey.OP_READ);
        }catch (IOException e){
            channel.close();
            selectionKey.cancel();
            e.printStackTrace();
        }

    }

    private static void onRead(SelectionKey  selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        readBuffer.clear();
        int read = 0;
        try {
            read = socketChannel.read(readBuffer);
        } catch (IOException e) {
            selectionKey.cancel();
            socketChannel.close();
            e.printStackTrace();
        }
        String str = new String(readBuffer.array(),0,read);
        System.out.println("服务器收到："+str);
        socketChannel.register(selector,SelectionKey.OP_WRITE);

    }

    /**
     * 当接受到accept（接受连接）事件的时候
     * 开始接受连接的时候意味着有数据要进来了，需要转为读事件
     * @param selectionKey
     */
    private static void onAccept(SelectionKey selectionKey) throws IOException {

        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
    }

}
