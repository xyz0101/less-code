package com.jenkin.proxy.server.entities;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;

import static com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils.ENTER;

@Data
public class HttpResponseContent extends HttpRequestResponseCommonPart{
    private String statusMsg;
    private int statusCode;

    public HttpResponseContent(){

    }



}