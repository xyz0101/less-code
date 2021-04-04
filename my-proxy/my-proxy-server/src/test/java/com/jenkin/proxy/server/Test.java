package com.jenkin.proxy.server;

import com.jenkin.proxy.server.entities.HeaderContent;
import com.jenkin.proxy.server.entities.HttpRequestParam;
import com.jenkin.proxy.server.entities.SocketConnectResponse;
import com.jenkin.proxy.server.utils.HttpUtils;

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
        httpRequestParam.setUrl("http://tencent.jenkin.tech:8000/polyfills.2e89f9530eab053a7df4.js");
        httpRequestParam.setHeader(HeaderContent.build()
                .head("cache-control","no-cache")
                .head("Postman-Token","65355f40-05af-41cb-b276-b1246e1136ff")
                .head("User-Agent","PostmanRuntime/7.6.0")
                .head("Accept","*/*")
                .head("Host","tencent.jenkin.tech:8000")
                .head("accept-encoding","gzip, deflate")
                .head("Connection","keep-alive")
                .head("token","kTqzFtkEPUufjkgKf9WBYIaRvII6MXp6E7m3LIa9sBxbj7eLPE/76AhJ4GB8AQMKniBwtyup2JF6+pBmuat3yd5woIPUDZcXqGChnGkPVgLL/A9oEVaaspOchnnKbUHJ7CqHWEMHoqcHzrG3H5HmjXuM5nYl776XJyEDA63ZO4kBwHVHZ2J743jPX59dybGGc2vWvI++CVW9koE3YMW0MKJ0NmwSrEUlIgeiaInzR6H8H981Zy3yNaJzpB403j7k")
        );
        httpRequestParam.setProtocolVersion("HTTP/1.1");
        httpRequestParam.setRequestType("GET");
        SocketConnectResponse socketConnectResponse =
                HttpUtils.requestBySocket(httpRequestParam);


        InputStream inputStream = socketConnectResponse.getInputStream();

        readByEnter(inputStream);

        inputStream.close();
        socketConnectResponse.closeAll();

    }

    private static void readByEnter(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] b = new byte[2048];
        int off=0;
        int len = b.length;

        while(true) {
            int c = inputStream.read();
            if (c == -1) {
                break;
            }
            b[off] = (byte) c;
            int i = 1;
            try {
                for (; i < len; i++) {
                    c =inputStream.read();
                    if (c == -1) {
                        break;
                    }
                    b[off + i] = (byte) c;
                }
            } catch (IOException ee) {
            }
            byteArrayOutputStream.write(b);
        }
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
    }

}
