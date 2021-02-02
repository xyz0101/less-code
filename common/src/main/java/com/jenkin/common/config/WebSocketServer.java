package com.jenkin.common.config;

import com.jenkin.common.constant.WebsocketConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author jenkin
 */
@ServerEndpoint(value = "/ws/{user}",subprotocols = {"protocol"})
@Component
public class WebSocketServer {
    private Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user) {
        logger.info("建立连接：{}，{}",user,session.getId());
        Map<String, Session> sessionMap = WebsocketConst.WEBSOCKET_SESSIONS.get(user);
        sessionMap = sessionMap==null?new HashMap<>():sessionMap;
        sessionMap.put(session.getId(),session);
        WebsocketConst.WEBSOCKET_SESSIONS.put(user,sessionMap);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("user") String user) {

        if (session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("连接关闭：{}, {}",user,session.getId());
        }else{
            logger.info("连接已经关闭：{}, {}",user,session.getId());

        }


        Map<String, Session> sessionMap = WebsocketConst.WEBSOCKET_SESSIONS.get(user);
        if (sessionMap!=null) {
            sessionMap.remove(session.getId());
        }


    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("user") String user) {
        logger.info("收到消息：{}，{}，msg：{}",user,session.getId(),message);
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error, @PathParam("user") String user) {

    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {

    }


}
