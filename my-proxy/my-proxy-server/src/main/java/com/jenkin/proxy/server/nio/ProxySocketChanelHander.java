package com.jenkin.proxy.server.nio;

import cn.hutool.core.io.IoUtil;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

import static com.jenkin.proxy.server.constant.Const.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/10 19:48
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ProxySocketChanelHander implements Runnable {
    private ArrayBlockingQueue<ByteBuffer> writeBlockQueue = new ArrayBlockingQueue<>(1000);
    private  ArrayBlockingQueue<Boolean> connectStatus = new ArrayBlockingQueue<>(1);

    private  static final ExecutorService WRITE_SERVICE = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());

    private Selector selector;
    private SocketChannel proxySocketChanel;
    private SocketChannel socketChannel;
    private String key;
    private String host="";
    private ByteBuffer head;
    private boolean isSSL;
    private Thread thread ;
    public ProxySocketChanelHander(String host,int port,SocketChannel socketChannel,boolean isSSL,ByteBuffer head){
        try {
            this.isSSL=isSSL;
            this.key = host+":"+port;
            System.out.println("初始化连接: "+key);
            this.head=head;
            selector = Selector.open();
            proxySocketChanel = SocketChannel.open();
            proxySocketChanel.configureBlocking(false);
            proxySocketChanel.connect(new InetSocketAddress(host,port));
            proxySocketChanel.register(selector, SelectionKey.OP_CONNECT);
            this.socketChannel = socketChannel;

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private boolean startReadProxy() {
        try {
            ByteBuffer readBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
            System.out.println("准备进入请求循环");
            while(proxySocketChanel.isConnected()){
                int read = proxySocketChanel.read(readBuffer);
                if(read>0){
                    //读
                    readBuffer.flip();
                    int size = readBuffer.remaining();
                    byte[] res = new byte[size];
                    ByteBuffer buffer = readBuffer.get(res);

                    System.out.println("请求得到内容："+new String(res)+"\n "+buffer);
                    writeToSocketChannel(ByteBuffer.wrap(res));
                    //写
                    readBuffer.flip();

                }else if(read<0){
                    System.out.println("关闭代理");
                   disConnect();
                    return false;
                }else{

                    System.out.println("读到 0");
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean isAlive(){
        Boolean take=null;
        try {
            take = connectStatus.take();
            connectStatus.put(take);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("代理是否存活：："+take);
        return take==null?false:take;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @SneakyThrows
    @Override
    public void run() {
        thread=Thread.currentThread();
        boolean write = false;
        while(true){
            try {
                System.out.println("代理选择事件阻塞住了 ");
                selector.select();
                System.out.println("代理选择事件 ");
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (!selectionKey.isValid()) continue;
                    if(selectionKey.isConnectable()){

                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        boolean b = channel.finishConnect();
                        connectStatus.put(b);
                        System.out.println("代理连接成功    "+b);
                        proxySocketChanel.register(selector,SelectionKey.OP_WRITE);
                    }
                    if (selectionKey.isWritable()){
                        System.out.println("代理写事件");
                        if(!write) {
                            write=true;
                            if (!isSSL) {
                                head = selectionKey.attachment() == null ? head : (ByteBuffer) selectionKey.attachment();
                                writeBlockQueue.put(head);

                            }
                            WRITE_SERVICE.execute(() -> {
                                while (true) {
                                    try {
                                        ByteBuffer buffer = writeBlockQueue.take();
                                        proxyRequest(buffer);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        proxySocketChanel.register(selector,SelectionKey.OP_READ);
                    }
                    if(selectionKey.isReadable()){
                        System.out.println("代理读事件");
//                        WRITE_SERVICE.execute(this::startReadProxy);
                        if(!startReadProxy()){
                            selectionKey.cancel();
                        }
                        System.out.println("代理读事件--完成--");
//                        proxySocketChanel.register(selector,SelectionKey.OP_WRITE);
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
                break;
            }

        }
        disConnect();
        System.out.println("代理线程结束");
    }

    private void disConnect() throws InterruptedException {
        System.out.println("断开连接");
        if(connectStatus.peek()!=null){
            connectStatus.take();
        }
        connectStatus.put(false);
//        IoUtil.close(selector);
        IoUtil.close(proxySocketChanel);
//        IoUtil.close(socketChannel);
    }

    public void registerWrite(ByteBuffer byteBuffer){

        try {
            writeBlockQueue.put(byteBuffer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void proxyRequest(ByteBuffer byteBuffer) throws InterruptedException {
        if (byteBuffer==null) return;
        try {
            if (byteBuffer.position()>0) byteBuffer.flip();
            System.out.println("准备代理的buffer "+byteBuffer+"  线程：："+Thread.currentThread().getId());
            proxySocketChanel.write(byteBuffer);

        } catch (Exception e) {
            e.printStackTrace();

            disConnect();
        }
    }

    private void writeToSocketChannel(ByteBuffer byteBuffer) throws IOException {
//        byteBuffer.flip();
        this.socketChannel.write(byteBuffer);

    }






    public String getKey() {
        return key;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
