//package com.jenkin.proxy.server;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStreamWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.Date;
//​
//​
//public class WebStation {
// //换行
// private static final String ENTER = "rn";
// //空格
// private static final String SPACE = " ";
// //是否关机
// private boolean isShutDown = false;
//
// /**
// * 指定服务器端口
// * @param args
// */
// public void start(int port) {
// try {
// ServerSocket serverSocket = new ServerSocket(port);
// //接收来自浏览器的请求
// this.recevie(serverSocket);
// } catch (IOException e) {
// stop();
// }
// }
// /**
// * 关闭服务器
// */
// private void stop() {
// isShutDown = true;
// }
// /**
// * 接受客户端消息
// */
// private void recevie(ServerSocket serverSocket) {
// try {
// //只要不关机就接收客户请求
// while (!isShutDown) {
// final Socket client = serverSocket.accept();  //接受客户
// //开启一个线程去执行客户请求
// new Thread() {
// public void run() {
// try {
// InputStream is = client.getInputStream();  //获得客户的输入流
//
// //返回给客户端的响应内容
// String clientContent = "<a href='https://m.sohu.com/a/333879258_100204013'>郑雅菱的心理活动，看看去！</a>";
//
// //输出描述内容
// StringBuilder headerInfo = new StringBuilder();
// headerInfo.append("HTTP/1.1").append(SPACE).append(200).append(SPACE);
// headerInfo.append("OK").append(ENTER);
// headerInfo.append("Server:zyl").append(SPACE).append("0.0.1v").append(ENTER);
// headerInfo.append("Date:Sat,"+SPACE).append(new Date()).append(ENTER);
// headerInfo.append("Content-Type:text/html;charset=UTF-8").append(ENTER);
// headerInfo.append("Content-Length:").append((clientContent+ENTER).toString().getBytes().length).append(ENTER);
// headerInfo.append("Access-Control-Allow-Origin:").append("*").append(ENTER);
// headerInfo.append(ENTER);
//
// BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
// bw.append(headerInfo.toString());  //输出头部描述内容
// bw.append(clientContent);  //输出头部描述内容
// bw.append(ENTER);   //换行 内容输出完成
// bw.flush(); //内存->客户端
// bw.close(); //关闭输出流
// is.close(); //关闭输入流
// //输出到控制台
// System.out.println(headerInfo.toString());
// System.out.println(clientContent+ENTER);
// }catch(IOException e) {
// e.printStackTrace();
// }
// };
// }.start();
// }
// }catch (IOException e) {
// e.printStackTrace();
// isShutDown = true;
// }
// }
//
// public static void main(String[] args) {
// //创建web初始站点
// WebStation server = new WebStation();
// //开启服务站点
// server.start(80);
// }
//}
//​```
//
//​
//进行了如下操作：
//*   安装了VM和CentOS
//
//*   打开终端，并进行网络配置
//
//*   安装JDK
//
//*   代码编译
//
//    *   touch WebStation.java
//
//    *   javac WebStation.java
//
//*   运行起初Web站点
//
//    *   java WebStation
//
//*   校验服务是否正常启动
//
//    *   netstat -tlnup|grep 80|
//
//    *   jps
//
//*   查询自己的IP地址
//
//    *   在终端里输入ifconfig -a命令在回车键
//
//
//
//##### 结果截图
//
//打开浏览器，输入当前IP地址+80端口号。结果如下图所示。
//
//![image.png](/img/bVbMHjb)
//
//点击超链接
//
//![image.png](/img/bVbMHjl)
//
//
//
