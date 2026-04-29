package org.example.test1.common.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("WebSocket连接建立: sid={}", sid);
        sessionMap.put(sid, session);
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("WebSocket连接关闭: sid={}", sid);
        sessionMap.remove(sid);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误", error);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到WebSocket消息: sid={}, message={}", sid, message);
    }

    public static void sendMessageToAll(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送WebSocket消息失败", e);
            }
        }
    }

    public static void sendMessageToUser(String sid, String message) {
        Session session = sessionMap.get(sid);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送WebSocket消息失败: sid={}", sid, e);
            }
        }
    }
}
