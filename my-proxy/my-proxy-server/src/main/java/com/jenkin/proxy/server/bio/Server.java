package com.jenkin.proxy.server.bio;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import sun.net.www.http.HttpClient;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2021/3/28 21:25
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Server {
    /**
     * 换行
      */
    private static final String ENTER = "\r\n";
    /**
     * 空格
      */
    private static final String SPACE = " ";
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
            //输出描述内容
            StringBuilder content = new StringBuilder();
            content.append("HTTP/1.1").append(SPACE).append(200).append(SPACE);
            content.append("OK").append(ENTER);
//            content.append("Server:zyl").append(SPACE).append("0.0.1v").append(ENTER);
            content.append("Date:Sat,"+SPACE).append(new Date()).append(ENTER);
            content.append("Content-Type:text/html;charset=UTF-8").append(ENTER);
            content.append("Content-Length:").append((sth+ENTER).getBytes().length).append(ENTER);
            content.append("Access-Control-Allow-Origin:").append("*").append(ENTER);
            content.append(ENTER);
            content.append(sth);
//            content.append(ENTER);
            System.out.println("响应数据 : \n"+sth );
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(content.toString().getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void readSth(Socket socket) {
        InputStream inputStream=null;
        InputStreamReader inputStreamReader=null;
        BufferedReader br=null;
        try {
            Map<String,String> header = new HashMap<>();
            String body = "测试输出";
            inputStream = socket.getInputStream();
            System.out.println("开始读数据");
            inputStreamReader = new InputStreamReader(inputStream);
            br = new BufferedReader(inputStreamReader);
            String s = null;
            while (((s=br.readLine())!=null)){
                System.out.println(s);
                if("".equals(s)){
                    System.out.println("header读取完成");
                    String contentLen = header.get("content-length");
                    contentLen=contentLen==null?"0":contentLen;
                    int len = Integer.parseInt(contentLen);
                    char[] res = new char[len];
                    br.read(res);
                    String resVal = new String(res);
                    System.out.println("结果：\n"+ resVal);



                    body= resVal;
                    break;
                }else{
                    if(s.contains(": ")) {
                        String[] split = s.split(": ");
                        header.put(split[0], split[1]);
                    }
                }
            }
            testRequestUrl(socket,body);
//            writeSth(socket,body);



        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IoUtil.close(socket);
            IoUtil.close(inputStream);
            IoUtil.close(inputStreamReader);
            IoUtil.close(br);
        }
    }

    private void testRequestUrl(Socket socket,String url) {
        HttpResponse execute = HttpUtil.createGet(url).execute().sync();
        Map<String, List<String>> headers = execute.headers();
        byte[] bytes = execute.bodyBytes();
        try {
            //输出描述内容
            StringBuilder content = new StringBuilder();
            content.append("HTTP/1.1").append(SPACE).append(200).append(SPACE);
            content.append("OK").append(ENTER);

            headers.forEach((k,v)->{
                if (k!=null/*&&!"Content-Length".equalsIgnoreCase(k)*/  ){
                    content.append(k).append(":").append(v.stream().collect(Collectors.joining(","))).append(ENTER);

                }
            });
//            content.append("Content-Length").append(":").append( bytes.length).append(ENTER);

             content.append(ENTER);
            byte[] headBytes = content.toString().getBytes();

            byte[] res = ArrayUtil.addAll(headBytes, bytes);

            OutputStream outputStream = socket.getOutputStream();
            System.out.println("响应数据： \n"+new String(res));
            outputStream.write(res);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
