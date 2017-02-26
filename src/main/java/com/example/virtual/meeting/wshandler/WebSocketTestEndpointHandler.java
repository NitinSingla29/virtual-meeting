package com.example.virtual.meeting.wshandler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;

@Component
public class WebSocketTestEndpointHandler extends AbstractWebSocketHandler {

    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(new TextMessage("Server Response is " + message.getPayload()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}