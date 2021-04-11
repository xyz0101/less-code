package com.jenkin.proxy.server.entities;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.net.url.UrlPath;
import cn.hutool.core.util.URLUtil;
import lombok.Data;

import java.net.*;

import static com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils.ENTER;
import static com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils.SPACE;


@Data
public class HttpRequestParam extends HttpRequestResponseCommonPart{
    private String requestType;
    private String url;

    public HttpRequestParam(){

    }

    public boolean isHttps(){
        return "CONNECT".equalsIgnoreCase(getRequestType())||getUrl().startsWith("https:");
    }
    public boolean isConnectType(){
        return "CONNECT".equalsIgnoreCase(getRequestType());
    }

    public String getRequestType(){
        if(this.requestType!=null) return this.requestType;
        initStatusProp();
        return this.requestType;
    }

    private void initStatusProp() {

        String firstLine = this.getStatusLine();
        String[] arr = firstLine.split(" ");
        this.setRequestType(arr[0]);
//        this.setUrl(isConnectType()?"https://"+arr[1]:arr[1]);
        this.setUrl(arr[1]);
        super.setProtocolVersion(arr[2]);

    }

    public String getUrl(){
        if(this.url!=null) return this.url;
        initStatusProp();
        return this.url;
    }
    public String getProtocolVersion(){
        if(super.getProtocolVersion()!=null) return super.getProtocolVersion();
        initStatusProp();
        return super.getProtocolVersion();
    }



    public String getPath(){
        String path = URLUtil.getPath(this.getUrl());
        return path==null?"":path;
    }


    public URI getHost() throws MalformedURLException, URISyntaxException {
        String host = getHeader().getValue("host");
        System.out.println("当前解析host："+host);
        try {
            URL url = new URL(host);
            return url.toURI();
        }catch (Exception e){


        }
       return host.endsWith(":443")?new URL("https://"+host).toURI():new URL("http://"+host).toURI();

    }




    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();
        content.append(getStatusLine()).append(ENTER);
//        content.append(getRequestType()).append(SPACE).append(getPath());
////        try {
////
////            String query = getHost().getQuery();
////            if (query!=null&&query.length()>0){
////                content.append("?").append(query);
////            }
////        } catch (MalformedURLException | URISyntaxException e) {
////            e.printStackTrace();
////        }
//        content.append(SPACE).append(getProtocolVersion()).append(ENTER);
        content.append(getHeader().toString());
        if (getBody()!=null)
        content.append(new String(getBody()));
        return content.toString();
    }


}