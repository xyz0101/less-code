package com.jenkin.proxy.server.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import com.jenkin.proxy.server.entities.HeaderContent;
import com.jenkin.proxy.server.entities.HttpRequestParam;
import com.jenkin.proxy.server.entities.HttpResponseContent;
import com.jenkin.proxy.server.entities.SocketConnectResponse;
import lombok.Builder;
import lombok.Data;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/3 12:51
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class HttpUtils {



    public static HttpResponseContent request(HttpRequestParam requestParam) throws URISyntaxException, IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpRequestBase requestBase = getRequestType(requestParam);

        if (requestBase==null) {
            return null;
        }
        HttpResponse execute = client.execute(requestBase);
        return convertHttpResponse(execute);

    }

    public static SocketConnectResponse requestBySocket(HttpRequestParam requestParam) throws IOException, URISyntaxException {
        System.out.println("请求参数首部："+requestParam.getHeader());
        Socket socket = new Socket();
        URI host = requestParam.getHost();
        socket.connect(new InetSocketAddress(host.getHost(), host.getPort()==-1?80:host.getPort()));
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(requestParam.getTotalBody());
        outputStream.flush();
        SocketConnectResponse socketConnectResponse = new SocketConnectResponse();
        socketConnectResponse.setInputStream(inputStream);
        socketConnectResponse.setOutputStream(outputStream);
        socketConnectResponse.setSocket(socket);
        return socketConnectResponse;

    }

    private static HttpRequestBase getRequestType(HttpRequestParam requestParam) throws URISyntaxException {

        HttpRequestBase request = null;
        switch (requestParam.getRequestType()){
            case "GET":
                request = new HttpGet();
                break;
            case "POST":
                request = new HttpPost();
                ((HttpPost)request).setEntity(convertEntity(requestParam.getBody()));
                break;
            case "DELETE":
                request = new HttpDelete();
                break;
            case "PUT":
                request = new HttpPut();
                ((HttpPost)request).setEntity(convertEntity(requestParam.getBody()));
                break;
            default:
                return null;
        }
        request.setURI(new URI(requestParam.getUrl()));
        request.setHeaders(convertHeader(requestParam.getHeader()));
        request.setProtocolVersion(convertProtocol(requestParam.getProtocolVersion()));

        return request;
    }


    private static HttpResponseContent convertHttpResponse(HttpResponse execute) throws IOException {

        HttpResponseContent httpResponseContent = new HttpResponseContent();
        if(execute.getEntity()!=null) {
            httpResponseContent.setBody(IoUtil.readBytes(execute.getEntity().getContent()));
        }
        httpResponseContent.setHeader(convertToHeader(execute.getAllHeaders()));
        httpResponseContent.setStatusCode(execute.getStatusLine().getStatusCode());
        httpResponseContent.setStatusMsg(execute.getStatusLine().getReasonPhrase());
        httpResponseContent.setProtocolVersion(execute.getStatusLine().getProtocolVersion().toString());
        return httpResponseContent;
    }

    private static HeaderContent convertToHeader(org.apache.http.Header[] allHeaders) {
        HeaderContent header = HeaderContent.build();
        for (org.apache.http.Header allHeader : allHeaders) {
            header.head(allHeader.getName(),allHeader.getValue());
        }
        return header;
    }

    private static ProtocolVersion convertProtocol(String protocolVersion) {

        String[] s = protocolVersion.split(" ");
        if(s.length>1){
            String s1 = s[s.length - 1];
            if (s1.endsWith("1.1")){
                return HttpVersion.HTTP_1_1;
            }else if(s1.endsWith("1.0")){
                return HttpVersion.HTTP_1_0;
            }else if(s1.endsWith("0.9")){
                return HttpVersion.HTTP_0_9;
            }
        }
        return HttpVersion.HTTP_1_1;
    }

    private static HttpEntity convertEntity(byte[] body) {
        if(ArrayUtil.isEmpty(body)) {
            return null;
        }
        BasicHttpEntity httpEntity = new BasicHttpEntity();
        httpEntity.setContent(new ByteArrayInputStream(body));
        httpEntity.setContentLength(body.length);
        return httpEntity;
    }

    private static org.apache.http.Header[] convertHeader(HeaderContent header) {
        Set<String> keys = header.keys();
        org.apache.http.Header[] headers = new org.apache.http.Header[keys.size()];
        int i=0;
        for (String key : keys) {
            headers[i++]=new BasicHeader(key,header.getValue(key));
        }
        return headers;
    }







}
