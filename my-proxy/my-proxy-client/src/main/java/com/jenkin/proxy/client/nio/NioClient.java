package com.jenkin.proxy.client.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/6 21:12
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class NioClient {
    static ByteBuffer readBuffer =  ByteBuffer.allocate(1024);
    static ByteBuffer writeBuffer =  ByteBuffer.allocate(1024);
    private static Selector selector;
    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {

        try {
            SocketChannel  socketChannel = SocketChannel.open();
            //设置为异步非阻塞
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost",15556));
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            chooseSelect(selector,socketChannel);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void chooseSelect(Selector selector, SocketChannel socketChannel) throws IOException {
        while(true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (!selectionKey.isValid()) {
                    continue;
                }
                if (selectionKey.isConnectable()) {
                    onConnect(selectionKey);
                    break;
                }
                if (selectionKey.isReadable()) {
                    onRead(selectionKey);
                }
                if (selectionKey.isWritable()) {

                    onWrite(selectionKey);
                }
                iterator.remove();

            }
        }

    }

    private static void onWrite(SelectionKey  selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        String write = scanner.nextLine();
        System.out.println("我发送："+write);
        writeBuffer.clear();
        writeBuffer.put(write.getBytes());
        writeBuffer.flip();//反转，由写变为读
        channel.write(writeBuffer);
        //注册读操作 下一次进行读
        channel.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ);
    }

    private static void onRead(SelectionKey  selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        int read = 0;
        try {
            read = socketChannel.read(readBuffer);
        } catch (IOException e) {
            selectionKey.cancel();
            socketChannel.close();
            e.printStackTrace();
        }
        String str = new String(readBuffer.array(),0,read);
        System.out.println("服务端给我返回了："+str);
        socketChannel.register(selector,SelectionKey.OP_WRITE);

    }

    /**
     * 当接受到accept（接受连接）事件的时候
     * 连接成功的时候意味着有数据要进来了，需要转为写事件
     * @param selectionKey
     */
    private static void onConnect(SelectionKey selectionKey) throws IOException {

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        socketChannel.finishConnect();
        socketChannel.register(selector,SelectionKey.OP_WRITE);
    }



}
