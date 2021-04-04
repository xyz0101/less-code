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


    public String getRequestType(){
        if(this.requestType!=null) return this.requestType;
        initStatusProp();
        return this.requestType;
    }

    private void initStatusProp() {

        String firstLine = this.getStatusLine();
        String[] arr = firstLine.split(" ");
        this.setRequestType(arr[0]);
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
        return URLUtil.getPath(this.getUrl());
    }


    public URI getHost() throws MalformedURLException, URISyntaxException {
        String u = this.getUrl();
        try {
            System.out.println("解析URL："+u);
            URL url = new URL(u);
            return url.toURI();
        }catch (Exception e){


        }
       return u.endsWith(":443")?new URL("https://"+u).toURI():new URL("http://"+u).toURI();

    }




    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();
        content.append(getRequestType()).append(SPACE).append(getPath());
        try {

            String query = getHost().getQuery();
            if (query!=null&&query.length()>0){
                content.append("?").append(query);
            }
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        content.append(SPACE).append(getProtocolVersion()).append(ENTER);
        content.append(getHeader().toString());
        if (getBody()!=null)
        content.append(new String(getBody()));
        return content.toString();
    }


}