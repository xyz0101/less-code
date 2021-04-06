package com.jenkin.proxy.server.bio;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.jenkin.proxy.server.entities.*;
import com.jenkin.proxy.server.utils.HttpUtils;
import com.jenkin.proxy.server.utils.HttpsResponseEndCounter;
import com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils;
import sun.net.www.http.HttpClient;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.jenkin.proxy.server.constant.Const.*;
import static com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils.ENTER;
import static com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils.SPACE;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/28 21:25
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class BioServer {



    private  static final ExecutorService CONNECT_SERVICE = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());
    private  static final ExecutorService REQUEST_SERVICE = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        new BioServer().startServer(15555);
    }


    public void startServer(int port){
        ServerSocket serverSocket=null;
        try {
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
            while (true){
                Socket accept = serverSocket.accept();
                System.out.println("服务端接受连接："+ accept.getInetAddress().getHostAddress());
                CONNECT_SERVICE.submit(new Worker(accept,"服务端连接线程"));
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

            //-------------------请求头的读取部分-----start-----------------------------------------
            inputStream = socket.getInputStream();
            System.out.println("开始读数据");
            HttpRequestResponseCommonPart commonPart = SocketRequestToHttpRequestUtils.readHeader(inputStream);
            HttpRequestParam requestParam = BeanUtil.copyProperties(commonPart, HttpRequestParam.class);
            Socket proxySocket =HttpUtils.getSocket(requestParam);
            socketConnectResponse = HttpUtils.requestBySocket(requestParam,proxySocket);
            OutputStream proxySocketOutputStream = proxySocket.getOutputStream();
            //----------------------响应HTTPS的CONNECT请求-------------------------------
            if (requestParam.isConnectType()){
                    writeBytes(socket,"HTTP/1.1 200 Connection Established\r\n\r\n".getBytes());
                    System.out.println("回复HTTPS的CONNECT");
            }else{
                proxySocketOutputStream.write(requestParam.getTotalBody());

            }
            //-------------------请求头的读取部分-----end-----------------------------------------

            //--------------------读取客户端的请求，写给代理请求-------start------------------------------
            REQUEST_SERVICE.execute(()->{
                while(socket.isConnected()){
                    try {
                        byte[] buffer  = new byte[65535];
                        int temp = 0;
                        while(true){
                            InputStream socketInputStream = socket.getInputStream();
                            try {
                                int available = socketInputStream.available();
                                if (available!=0){
                                    SocketRequestToHttpRequestUtils.readBytesByArr(socketInputStream,available,buffer);
                                }else{
                                    int read = socketInputStream.read();
                                    if (read==-1) {
                                        break;
                                    }else{
                                        System.out.println("写出代理端-1");
                                        proxySocketOutputStream.write(read);
                                    }
                                }
                                System.out.println("写出代理端-2");
                                proxySocketOutputStream.write(buffer,0,available);
                            }catch (Exception e){
                            }
                        }
                        buffer=null;

                    } catch (Exception e) {

                        break;
                    }
                }
            });
            //--------------------读取客户端的请求，写给代理请求-------end------------------------------

//            SocketRequestToHttpRequestUtils.writeInputStreamByCounter(proxySocket.getInputStream(),socket.getOutputStream(),new HttpsResponseEndCounter());

            //--------------------读取代理请求的响应，写给客户端求-------start------------------------------

            byte[] buffer  = new byte[65535];
            int temp = 0;
             while(socket.isConnected()&&!proxySocket.isInputShutdown()){
                 InputStream proxySocketInputStream = proxySocket.getInputStream();
                 try {
                    int available = proxySocketInputStream.available();
                    if (available!=0){
                        SocketRequestToHttpRequestUtils.readBytesByArr(proxySocketInputStream,available,buffer);
                    }else{
                        int read = proxySocketInputStream.read();
                        if (read==-1) {
                            break;
                        }else{
                            System.out.println("写出客户端");
                            socket.getOutputStream().write(read);
                        }
                    }
//                     System.out.println("写出客户端");
                    socket.getOutputStream().write(buffer,0,available);
                }catch (Exception e){
                }
            }
            buffer=null;
            //--------------------读取代理请求的响应，写给客户端求-------end------------------------------

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
