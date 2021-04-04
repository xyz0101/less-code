package com.jenkin.proxy.server.entities;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;

import static com.jenkin.proxy.server.utils.SocketRequestToHttpRequestUtils.ENTER;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/4 15:55
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
public class HttpRequestResponseCommonPart {
    private String statusLine;
    private String protocolVersion;
    private byte[] body;
    private HeaderContent header;

    public byte[] getTotalBody(){
        return ArrayUtil.addAll((getStatusLine()+ENTER).getBytes() ,getHeader().toString().getBytes(),getBody());
    }

}
