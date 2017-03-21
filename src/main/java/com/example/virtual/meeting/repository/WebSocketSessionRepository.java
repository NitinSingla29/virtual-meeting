package com.example.virtual.meeting.repository;

import org.springframework.web.socket.WebSocketSession;

public interface WebSocketSessionRepository {

    public void add(WebSocketSession webSocketSession);

    public WebSocketSession get(String webSocketSessionId);
}
