package com.jenkin.common.constant;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jenkin
 * @className WebsocketConst
 * @description TODO
 * @date 2020/9/23 15:25
 */
public class WebsocketConst {
    /**
     * websocket会话列表，key 为usercode
     */
    public static final Map<String, Map<String, Session>> WEBSOCKET_SESSIONS = new HashMap<>();
}
