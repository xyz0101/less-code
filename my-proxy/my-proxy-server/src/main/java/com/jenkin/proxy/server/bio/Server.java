package com.jenkin.proxy.server.bio;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.jenkin.proxy.server.entities.*;
import com.jenkin.proxy.server.utils.HttpUtils;
import com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils;
import sun.net.www.http.HttpClient;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils.ENTER;
import static com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils.SPACE;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/28 21:25
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Server {

    public static final int CORE_SIZE = Runtime.getRuntime().availableProcessors()*20;
    public static final int MAX_SIZE = Runtime.getRuntime().availableProcessors()*40;
    public static final int QUEUE_SIZE = 1000;
    public static final int ALIVE_TIME = 10;


    private  static final ExecutorService executorService = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        new Server().startServer(15555);
    }


    public void startServer(int port){
        ServerSocket serverSocket=null;
        try {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);

            while (true){
                Socket accept = serverSocket.accept();
                System.out.println("服务端接受连接："+ accept.getInetAddress().getHostAddress());
                executorService.submit(new Worker(accept,"服务端连接线程"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    class Worker extends Thread{
        private Socket socket;
        public Worker(Socket socket,String name){
            super(name);
            this.socket=socket;
        }

        @Override
        public void run() {
              readSthUseSocket(socket);
        }

    }
    private void writeBytes(Socket socket,byte[] bytes) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            System.out.println("返回成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void readSthUseSocket(Socket socket) {
        InputStream inputStream=null;
        InputStreamReader inputStreamReader=null;
        BufferedReader br=null;
        SocketConnectResponse socketConnectResponse=null;
        try {


            inputStream = socket.getInputStream();
            System.out.println("开始读数据");
            HttpRequestResponseCommonPart commonPart = SocketRequestToHttpRequestUtils.readHeader(inputStream);
            HttpRequestParam requestParam = BeanUtil.copyProperties(commonPart, HttpRequestParam.class);
            Socket proxySocket =HttpUtils.getSocket(requestParam);
            socketConnectResponse = HttpUtils.requestBySocket(requestParam,proxySocket);
            OutputStream proxySocketOutputStream = proxySocket.getOutputStream();
            if (requestParam.isConnectType()){
                    writeBytes(socket,"HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
                    System.out.println("回复HTTPS的CONNECT");
            }else{
                proxySocketOutputStream.write(requestParam.getTotalBody());

            }


            executorService.execute(()->{
                while(socket.isConnected()){
                    try {
                        proxySocketOutputStream.write(socket.getInputStream().read());
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            });
            while(socket.isConnected()&&proxySocket.isConnected()){
                socket.getOutputStream().write(proxySocket.getInputStream().read());
            }









//            HttpRequestParam requestParam =  SocketRequestToHttpRequestUtils.convertSocketRequestToRequestParam(inputStream);
//            Socket proxySocket =HttpUtils.getSocket(requestParam);
//            socketConnectResponse = HttpUtils.requestBySocket(requestParam,proxySocket);
            //这里为什么要while true？因为如果是keepalive的长连接是不能关闭连接的，可能还会继续发送
//            while(true){
//                socketConnectResponse = HttpUtils.requestBySocket(requestParam,proxySocket);
//                if (requestParam.isConnectType()){
//                    writeBytes(socket,"HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
//                    System.out.println("回复HTTPS的CONNECT");
//                    socketConnectResponse.setConnectRequest(false);
//
//                }else{
//                    HttpResponseContent responseContent = SocketRequestToHttpRequestUtils.convertSocketInputStreamResponseBodyToBytes(socketConnectResponse);
//                    byte[] bytes = responseContent.getTotalBody();
//                    System.out.println("响应体：\n"+new String(bytes));
//                    writeBytes(socket,bytes);
//                    if ("close".equals(responseContent.getHeader().getValue("Connection"))||socket.isClosed()){
//                        System.out.println("连接关闭");
//                        break;
//                    }
//                }
////                requestParam =  SocketRequestToHttpRequestUtils.convertSocketRequestToRequestParam(inputStream);
//            }





        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(socketConnectResponse!=null){

                socketConnectResponse.closeAll();
            }
            IoUtil.close(socket);
            IoUtil.close(inputStream);
            IoUtil.close(inputStreamReader);
            IoUtil.close(br);
        }
    }



}
