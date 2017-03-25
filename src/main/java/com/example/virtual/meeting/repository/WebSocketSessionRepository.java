package com.example.virtual.meeting.repository;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

public interface WebSocketSessionRepository {

    public Collection<WebSocketSession> findAll();

    public void add(WebSocketSession webSocketSession);

    public WebSocketSession get(String webSocketSessionId);

    public void remove(String webSocketSessionId);

    public void remove(WebSocketSession webSocketSession);
}
