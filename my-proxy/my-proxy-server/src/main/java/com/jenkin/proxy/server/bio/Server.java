package com.jenkin.proxy.server.bio;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.jenkin.proxy.server.entities.HeaderContent;
import com.jenkin.proxy.server.entities.HttpRequestParam;
import com.jenkin.proxy.server.entities.HttpResponseContent;
import com.jenkin.proxy.server.entities.SocketConnectResponse;
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

    public static final int CORE_SIZE = Runtime.getRuntime().availableProcessors()*2;
    public static final int MAX_SIZE = Runtime.getRuntime().availableProcessors()*4;
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

            HttpRequestParam requestParam =  SocketRequestToHttpRequestUtils.convertSocketRequestToRequestParam(inputStream);
            socketConnectResponse = HttpUtils.requestBySocket(requestParam);
            if (socketConnectResponse!=null) {
                byte[] bytes = SocketRequestToHttpRequestUtils.convertSocketInputStreamResponseBodyToBytes(socketConnectResponse).getTotalBody();
                System.out.println("响应体：\n"+new String(bytes));
                writeBytes(socket,bytes);


            }




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
