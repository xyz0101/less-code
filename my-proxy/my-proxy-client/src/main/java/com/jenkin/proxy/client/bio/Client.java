package com.jenkin.proxy.client.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/28 21:42
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Client {
    public static final int CORE_SIZE = Runtime.getRuntime().availableProcessors()*2;
    public static final int MAX_SIZE = Runtime.getRuntime().availableProcessors()*4;
    public static final int QUEUE_SIZE = 1000;
    public static final int ALIVE_TIME = 10;


    private  static final ExecutorService executorService = new ThreadPoolExecutor(CORE_SIZE,MAX_SIZE,ALIVE_TIME, TimeUnit.MINUTES,new ArrayBlockingQueue<>(QUEUE_SIZE),new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        Socket socket = client.connect("127.0.0.1", 15555);
        System.out.println("客户端启动成功");
//        client.writeSth(socket);
        client.dealSocket(socket);
        Thread.sleep(100000);
    }

    private void dealSocket(Socket socket) {
        executorService.submit(()->readSth(socket));
        executorService.submit(()->writeSth(socket));
    }

    private void writeSth(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            PrintWriter bw = new PrintWriter(outputStreamWriter);

            Scanner input = new Scanner(System.in);
            System.out.println("请输入：");
            while (input.hasNext()){
                String next = input.next();
                bw.println(next);
                bw.flush();
                System.out.println("发送消息");
                outputStream.flush();
                outputStreamWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readSth(Socket socket) {

        try {

                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);

                String s = null;
                while ((s=br.readLine())!=null) {
                    System.out.println("接收到服务端消息：" + s);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }


    public Socket connect(String ip,int port){

        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip,port));
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }


}
