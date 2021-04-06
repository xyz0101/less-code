package com.jenkin.proxy.server;

import com.jenkin.proxy.server.entities.HeaderContent;
import com.jenkin.proxy.server.entities.HttpRequestParam;
import com.jenkin.proxy.server.entities.HttpResponseContent;
import com.jenkin.proxy.server.entities.SocketConnectResponse;
import com.jenkin.proxy.server.utils.HttpUtils;
import com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/3 17:28
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class Test {


    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpRequestParam httpRequestParam = new HttpRequestParam();
        httpRequestParam.setUrl("api.zhihu.com:443");
        httpRequestParam.setHeader(HeaderContent.build()
//                .head("cache-control","no-cache")
//                .head("Postman-Token","65355f40-05af-41cb-b276-b1246e1136ff")
//                .head("User-Agent"," Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
//                .head("Accept","*/*")
//                .head("host"," api.zhihu.com:443")
//                .head("accept-encoding","gzip, deflate")
//                .head("Connection"," keep-alive")
//                .head("Connection","close")
//                .head("token","kTqzFtkEPUufjkgKf9WBYIaRvII6MXp6E7m3LIa9sBxbj7eLPE/76AhJ4GB8AQMKniBwtyup2JF6+pBmuat3yd5woIPUDZcXqGChnGkPVgLL/A9oEVaaspOchnnKbUHJ7CqHWEMHoqcHzrG3H5HmjXuM5nYl776XJyEDA63ZO4kBwHVHZ2J743jPX59dybGGc2vWvI++CVW9koE3YMW0MKJ0NmwSrEUlIgeiaInzR6H8H981Zy3yNaJzpB403j7k")
        );
        httpRequestParam.setProtocolVersion("HTTP/1.1");
        httpRequestParam.setRequestType("CONNECT");
        httpRequestParam.setStatusLine("CONNECT api.zhihu.com:443 HTTP/1.1");
        SocketConnectResponse socketConnectResponse =
                HttpUtils.requestBySocket(httpRequestParam,HttpUtils.getSocket(httpRequestParam));


        HttpResponseContent responseContent = SocketRequestToHttpRequestUtils.convertSocketInputStreamResponseBodyToBytes(socketConnectResponse);
        System.out.println(new String(responseContent.getTotalBody()));



         socketConnectResponse.closeAll();

    }


}
