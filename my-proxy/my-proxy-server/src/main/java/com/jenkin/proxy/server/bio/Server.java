package com.jenkin.proxy.server.bio;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
                readSth(accept);
//                executorService.submit(new Worker(accept,"服务端连接线程"));
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
//            writeSth(socket, s);


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
        try {

            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String s =null;
            while ((s=br.readLine())!=null){
                System.out.println("接收到客户端消息："+s);
                writeSth(socket,s);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }

}
