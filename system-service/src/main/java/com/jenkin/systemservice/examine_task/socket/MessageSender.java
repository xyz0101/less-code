package com.jenkin.systemservice.examine_task.socket;

import com.jenkin.common.utils.ApplicationContextProvider;

import javax.websocket.Session;
import java.util.Map;

/**
 * @author jenkin
 * @className MessageSender
 * @description TODO
 * @date 2020/9/23 16:12
 */
public interface MessageSender {
    String PREFIX ="WEBSOCKET-";
    /**
     * 出发状态变化
     */
    String TASK_STATUS="TASK-STATUS";
    /**
     * 出发四史学习的分值变化
     */
    String TASK_SSXX_GRADE="TASK-SSXX-GRADE";

    /**
     * 获取消息发送对象
     * @param code
     * @return
     */
    static MessageSender getMessageSender(String code){
        String id = PREFIX + code;
        if (ApplicationContextProvider.existBean(id)) {
            return (MessageSender) ApplicationContextProvider.getBean(id);
        }
        return null;
    }

    /**
     *发送信息到前端
     * @param msg
     * @param userId 用户编号
     */
    void sendMessage(String msg, String userId);

    /**
     * 发送消息
     * @param text
     * @param sessionMap
     */
    default void sendText(String text, Map<String, Session> sessionMap){
        if (sessionMap!=null) {
            for (Session value : sessionMap.values()) {
                if (value.isOpen()) {
                    try {
                        value.getBasicRemote().sendText(text);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
        }
    }

}
