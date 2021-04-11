package com.jenkin.proxy.server.nio;

import cn.hutool.core.bean.BeanUtil;
import com.jenkin.proxy.server.entities.HttpRequestParam;
import com.jenkin.proxy.server.entities.HttpRequestResponseCommonPart;
import com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
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
    /**
     * 申请一个8k 的空间读取数据，我们需要用首行来判断是否是CONNECT请求，8k够了
     */
    static ByteBuffer readBuffer =  ByteBuffer.allocate(10);
    static ByteBuffer writeBuffer =  ByteBuffer.allocate(8096);
    private static Selector selector;
    private  static final ExecutorService CONNECT_SERVICE = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());
    private  static final ExecutorService READ_SERVICE = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());
    private  static final ExecutorService WRITE_SERVICE = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {

        CONNECT_SERVICE.execute(new ServerHandler(15556));
//        try {
//            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//            //设置为异步非阻塞
//            serverSocketChannel.configureBlocking(false);
//            serverSocketChannel.bind(new InetSocketAddress(15556));
//            selector = Selector.open();
//            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//            chooseSelect(selector,serverSocketChannel);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


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
//                        CONNECT_SERVICE.execute(()->{
                            try {
                                onAccept(selectionKey);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                        });

                    }
                    if (selectionKey.isReadable()) {
//                        READ_SERVICE.execute(()->{
                            try {
                                onRead(selectionKey);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                        });

                    }
                    if (selectionKey.isWritable()) {
                        WRITE_SERVICE.execute(()->{
                            try {
                                onWrite(selectionKey);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
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


        try {

            System.out.println("服务器写数据"+selectionKey.attachment());
            if (selectionKey.attachment()!=null)
            channel.write( ByteBuffer.wrap((byte[]) selectionKey.attachment()));
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
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        int read = -1;
        try {
            System.out.println(selectionKey.attachment());
            read = socketChannel.read(byteBuffer);
            byte[] array = byteBuffer.get(new byte[byteBuffer.remaining()]).array();
            System.out.println(new String(array)+"哈希值："+ socketChannel.hashCode());
            selectionKey.attach("attach");

        } catch (IOException e) {
            selectionKey.cancel();
            socketChannel.close();
            e.printStackTrace();
        }

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
