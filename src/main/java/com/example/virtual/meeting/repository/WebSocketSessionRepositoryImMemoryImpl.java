package com.example.virtual.meeting.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionRepositoryImMemoryImpl implements WebSocketSessionRepository {

    private Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void add(WebSocketSession webSocketSession) {
        sessionMap.put(webSocketSession.getId(),webSocketSession);
    }

    @Override
    public WebSocketSession get(String webSocketSessionId) {
        return sessionMap.get(webSocketSessionId);
    }
}
