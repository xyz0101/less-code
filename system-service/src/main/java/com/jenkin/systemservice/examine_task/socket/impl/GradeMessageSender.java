package com.jenkin.systemservice.examine_task.socket.impl;


import com.jenkin.common.constant.WebsocketConst;
import com.jenkin.systemservice.examine_task.socket.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Map;

import static com.jenkin.common.constant.ThreadPoolConst.ASYNC_JOBS_EXECUTORS;
import static com.jenkin.systemservice.examine_task.socket.MessageSender.TASK_SSXX_GRADE;
import static com.jenkin.systemservice.examine_task.socket.MessageSender.TASK_STATUS;

/**
 * @author jenkin
 * @className TaskStatusMessageSender
 * @description TODO
 * @date 2020/9/23 16:20
 */
@Component(MessageSender.PREFIX+TASK_SSXX_GRADE)
public class GradeMessageSender implements MessageSender {
    private Logger logger = LoggerFactory.getLogger(GradeMessageSender.class);
    @Override
    public void sendMessage(String msg, String userId) {
        ASYNC_JOBS_EXECUTORS.execute(() -> {
            logger.info("发送成绩消息：{}",msg);
            Map<String, Session> sessionMap = WebsocketConst.WEBSOCKET_SESSIONS.get(userId);
            sendText(msg,sessionMap);
        });
    }
}
