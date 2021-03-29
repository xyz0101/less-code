package com.jenkin.proxy.server.bio;

import cn.hutool.core.io.IoUtil;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

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
              readSth(socket);
        }

    }
    private void writeSth(Socket socket,String sth) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            PrintWriter bw = new PrintWriter(outputStreamWriter);
            bw.println("服务端回应了："+sth);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readSth(Socket socket) {
        InputStream inputStream=null;
        InputStreamReader inputStreamReader=null;
        BufferedReader br=null;
        try {



            inputStream = socket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            br = new BufferedReader(inputStreamReader);
            String s =null;
            StringBuilder sb = new StringBuilder();
            int count =0;
            Map<String,String> header = new HashMap<>();
            boolean readContent = false;
            String s1 = IoUtil.readUtf8(inputStream);
            System.out.println(Thread.currentThread().getId()+"::"+"接收到客户端消息：\n"+s1);
            writeSth(socket,"HTTP/1.1 200 OK\n" +
                    "Content-Type: text/html;charset=UTF-8\n" +
                    "Content-Length: 101\n" +
                    "Date: Wed, 06 Jun 2018 07:08:42 GMT\n" +
                    "\n" +
                    "<html>\n" +
                    "\n" +
                    "  <head>\n" +
                    "    <title>$Title$</title>\n" +
                    "  </head>\n" +
                    "\n" +
                    "  <body>\n" +
                    "  hello , response\n" +
                    "  </body>\n" +
                    "</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IoUtil.close(socket);
            IoUtil.close(inputStream);
            IoUtil.close(inputStreamReader);
            IoUtil.close(br);
        }
    }

}
