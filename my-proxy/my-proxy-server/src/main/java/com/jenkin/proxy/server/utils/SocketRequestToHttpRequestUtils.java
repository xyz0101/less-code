package com.jenkin.proxy.server.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import com.jenkin.proxy.server.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/3 13:51
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class SocketRequestToHttpRequestUtils {

    private static Logger logger = LogManager.getLogger(SocketRequestToHttpRequestUtils.class);

    /**
     * 换行
     */
    public static final String ENTER = "\r\n";
    /**
     * 空格
     */
    public static final String SPACE = " ";
    /**
     * 内容长度
     */
    public static final String CONTENT_LENGTH="Content-Length";
    public static final String TRANSFER_ENCODING="Transfer-Encoding";

    private static final int BUFFER_SIZE=1024*1024*10;

    public static HttpRequestParam convertSocketRequestToRequestParam(InputStream inputStream) throws IOException {

        HttpRequestResponseCommonPart commonPart = readHeader(inputStream);
        HttpRequestParam httpRequestParam = BeanUtil.copyProperties(commonPart, HttpRequestParam.class);
        HeaderContent headerContent = httpRequestParam.getHeader();
        if (headerContent.containsKey(CONTENT_LENGTH)) {
            String value = headerContent.getValue(CONTENT_LENGTH);
            httpRequestParam.setBody(readBytesByLength(inputStream,Integer.parseInt(value)));
            return httpRequestParam;
        }else{

            if(headerContent.getValue(TRANSFER_ENCODING)!=null){
                System.out.println("是chunk 请求头：\n"+ headerContent);
                byte[] body=readChunkBody(inputStream);
                httpRequestParam.setBody(body);
            }

            return httpRequestParam;
        }
    }



    public static HttpResponseContent convertSocketInputStreamResponseBodyToBytes(SocketConnectResponse socketConnectResponse) throws IOException {

        HttpRequestResponseCommonPart commonPart = readHeader(socketConnectResponse.getInputStream());
        HttpResponseContent responseContent = BeanUtil.copyProperties(commonPart, HttpResponseContent.class);
        HeaderContent headerContent = responseContent.getHeader();

        if (headerContent.containsKey(CONTENT_LENGTH)) {
            String value = headerContent.getValue(CONTENT_LENGTH);
            responseContent.setBody(readBytesByLength(socketConnectResponse.getInputStream(),Integer.parseInt(value)));
            return responseContent;
        }else{
//            System.out.println("是chunk 响应头：\n"+responseContent.getHeader());
            byte[] body=new byte[0];
            if(responseContent.getHeader().getValue(TRANSFER_ENCODING)!=null){
                body=readChunkBody(socketConnectResponse.getInputStream());
            }
            responseContent.setBody(body);
            return responseContent;
        }


    }

    /**
     * 读取输入流，避免tcp半包
     * @param inputStream
     * @param len
     * @return
     * @throws IOException
     */
    private static byte[] readBytesByLength(InputStream inputStream,int len) throws IOException {
        byte[] body = new byte[len];
        int count = 0;
        do {
            count += inputStream.read(body, count, len - count);
        } while (count < len);
        System.out.println("不是chunk ,contentLength：："+len+"  已读："+count);
        return body;
    }

    private static byte[] readChunkBody(InputStream inputStream) throws IOException {
        int separatorCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int bytesRead = 0;
            int tmp = -2;

            //TODO 如果缓冲区太小会不会出现header未读完的情况
            while (bytesRead < BUFFER_SIZE && (tmp = inputStream.read()) != -1) {

                buffer[bytesRead++] = (byte) tmp;
                //当连续读取到7个\r或\n或0，（chunk数据通过 \r\n0\r\n\r\n 判断结尾）说明已经读到header的最后了，否则重置计数器
                if (tmp == '\r' || tmp == '\n' || tmp == '0') {
                    separatorCount++;
                } else {
                    separatorCount = 0;
                }
                if (separatorCount == 7) {
                    break;
                }
                if (bytesRead == BUFFER_SIZE) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    byteArrayOutputStream.flush();
                    bytesRead = 0;
                    System.out.println("缓冲区满，写入");
                }
            }
            byteArrayOutputStream.write(buffer, 0, bytesRead);
            byteArrayOutputStream.flush();
            buffer = null;
            buffer = byteArrayOutputStream.toByteArray();
            System.out.println(" chunk 读取完成 ，大小：" + buffer.length + " 线程：" + Thread.currentThread().getId());

        }
        return buffer;



    }


    public static HttpRequestResponseCommonPart readHeader(InputStream inputStream) throws IOException {
        int separatorCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        int tmp = -2;
        //TODO 如果缓冲区太小会不会出现header未读完的情况
        while(bytesRead < BUFFER_SIZE && (tmp = inputStream.read()) != -1){
            buffer[bytesRead++] = (byte)tmp;
            //当连续读取到4个\r或\n，说明已经读到header的最后了，否则重置计数器
            if(tmp == '\r' || tmp == '\n'){
                separatorCount++;
            }else{
                separatorCount = 0;
            }
            if(separatorCount == 4){
                break;
            }
        }

        return resolveHeader(buffer,bytesRead);
    }

    private static HttpRequestResponseCommonPart resolveHeader(byte[] buffer,int realLen) throws UnsupportedEncodingException {
        String headerString = "";
        HttpRequestResponseCommonPart responseContent = new HttpRequestResponseCommonPart();
        String currentLine = "";
        HeaderContent headerContent = HeaderContent.build();
        headerString = new String(buffer, 0,realLen , "UTF-8");
        int lineNum = 0;
        while(true){
            int i = headerString.indexOf(ENTER);
            if(i==-1) break;
            currentLine = headerString.substring(0, i);
            lineNum++;
            //已解析到最后的两个\r\n处，退出循环
            if("".equals(currentLine)){
                break;
            }
            if(lineNum==1){
                //首行为状态行
                responseContent.setStatusLine(currentLine);
            }else {
                headerContent.head(currentLine.substring(0, currentLine.indexOf(':')), currentLine.substring(currentLine.indexOf(':') + 2));
            }
            headerString = headerString.substring(headerString.indexOf(ENTER) + 2);
        }
        responseContent.setHeader(headerContent);
        return responseContent;
    }


    public static byte[] convertResponseBodyToBytes(HttpResponseContent responseContent){
        //输出描述内容
        StringBuilder content = new StringBuilder();
        content.append(responseContent.getProtocolVersion()).append(SPACE).append(responseContent.getStatusCode()).append(SPACE);
        content.append(responseContent.getStatusMsg()).append(ENTER);
        content.append(responseContent.getHeader().toString());
        content.append(ENTER);
        byte[] bytes = content.toString().getBytes();

        byte[] res = ArrayUtil.addAll(bytes, responseContent.getBody());
        if (responseContent.getHeader().getValue("Transfer-Encoding")!=null){
            responseContent.getHeader().head("Content-Length",res.length+"");
            responseContent.getHeader().remove("Transfer-Encoding");
        }
        return res;
    }

}
