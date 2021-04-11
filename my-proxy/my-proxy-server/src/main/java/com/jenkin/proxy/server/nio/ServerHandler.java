package com.jenkin.proxy.server.nio;

import cn.hutool.core.bean.BeanUtil;
import com.jenkin.proxy.server.entities.HttpRequestParam;
import com.jenkin.proxy.server.entities.HttpRequestResponseCommonPart;
import com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

import static com.jenkin.proxy.server.constant.Const.*;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/10 19:29
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ServerHandler implements Runnable{
    private  static final ExecutorService READ_SERVICE = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());
    private static final ConcurrentHashMap<String,ProxySocketChanelHander> CHANEL_HANDER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public ServerHandler(int port){
        try {
            selector =Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("NIO代理服务已启动");

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        System.out.println("开始连接");
        while(true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                System.out.println("选择事件");
                if (!selectionKey.isValid()) continue;
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel scc = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel accept = scc.accept();
                    accept.configureBlocking(false);
                    accept.register(selector,SelectionKey.OP_READ);
                    System.out.println("连接成功");
                }
                if(selectionKey.isReadable()){
                    System.out.println("开始读取-------------------------------------");
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
                    int read = channel.read(byteBuffer);
                    if(read>0){
                        byteBuffer.flip();
                        int size = byteBuffer.remaining();
                        byte[] bytes = new byte[size];
                        ByteBuffer buffer = byteBuffer.get(bytes);
//                        System.out.println("读取信息："+new String(buffer.array()));
                        buffer.flip();
                        boolean b = writeRequest(buffer, channel, selectionKey);

                        System.out.println("读取完成-------------------------------------");
                    }else if(read<0){
                        System.out.println("连接关闭");
                        channel.close();
                        selectionKey.cancel();

                    }else{
                        channel.register(selector,SelectionKey.OP_READ);
                    }

                }


            }
        }

    }
    private boolean writeRequest(ByteBuffer byteBuffer, SocketChannel socketChannel, SelectionKey selectionKey) throws IOException, URISyntaxException {
        ProxySocketChanelHander socketChanelHander =null;
        HttpRequestResponseCommonPart commonPart=null;
        ProxySocketChanelHander attachment = null;
        if (selectionKey.attachment()!=null){
            attachment= (ProxySocketChanelHander) selectionKey.attachment();
            byteBuffer.mark();
             commonPart = SocketRequestToHttpRequestUtils.readHeaderInByteBuffer(byteBuffer,byteBuffer.remaining());
            byteBuffer.reset();
            if(commonPart!=null){
                String host = commonPart.getHeader().getValue("host");
                if (host != null && commonPart.getHeader() != null && !attachment.getHost().equals(host)) {
                    System.out.println("主机变化 old:"+attachment.getHost()+" new:"+ host);
                    selectionKey.attach(CHANEL_HANDER_CONCURRENT_HASH_MAP.get(host));
                }
                if (selectionKey.attachment()!=null) {
                    attachment = (ProxySocketChanelHander) selectionKey.attachment();
                }
            }


            if(!(attachment).isAlive()){

                selectionKey.attach(null);
//                return false;
            }

        }else{
            byteBuffer.mark();
            commonPart = SocketRequestToHttpRequestUtils.readHeaderInByteBuffer(byteBuffer,byteBuffer.remaining());
            byteBuffer.reset();
        }

        if(selectionKey.attachment()==null) {
//            System.out.println(byteBuffer);
                HttpRequestParam requestParam = BeanUtil.copyProperties(commonPart, HttpRequestParam.class);
            System.out.println("请求参数：：："+ requestParam);
                URI host = requestParam.getHost();
                String ip = host.getHost();
                int port = host.getPort();
                if (requestParam.isConnectType()) {
                    byte[] bytes = "HTTP/1.1 200 Connection Established\r\n\r\n".getBytes();
                    socketChannel.write(ByteBuffer.wrap(bytes));
                    System.out.println("是https");
                    socketChanelHander = ProxySocketChanneFactory.getInstance(ip, port, true, socketChannel, null);
//                    System.out.println("https连接成功："+socketChanelHander.isAlive());
                } else {
                    System.out.println("不是https");
                    socketChanelHander = ProxySocketChanneFactory.getInstance(ip, port, false, socketChannel, byteBuffer);
                }
                socketChanelHander.setHost(requestParam.getHeader().getValue("host"));
                READ_SERVICE.execute(socketChanelHander);
                selectionKey.attach(socketChanelHander);
            CHANEL_HANDER_CONCURRENT_HASH_MAP.put(socketChanelHander.getHost(),socketChanelHander);
        }else{

            socketChanelHander = attachment;
            System.out.println("再次读取:host:::"+socketChanelHander.getHost());
            socketChanelHander.registerWrite(byteBuffer);

        }
        System.out.println("write 完成");
        socketChannel.register(selector,SelectionKey.OP_READ,socketChanelHander);
        return true;
    }
}
