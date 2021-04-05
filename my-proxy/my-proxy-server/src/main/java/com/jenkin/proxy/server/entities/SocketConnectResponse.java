package com.jenkin.proxy.server.entities;

import cn.hutool.core.io.IoUtil;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author ：jenkin
 * @date ：Created at 2021/4/3 16:45
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
public class SocketConnectResponse {

    private InputStream inputStream;
    private Socket socket;
    private OutputStream outputStream;
    private boolean isConnectRequest;

    public void closeAll(){

        IoUtil.close(inputStream);
        IoUtil.close(socket);
        IoUtil.close(outputStream);

    }
}
