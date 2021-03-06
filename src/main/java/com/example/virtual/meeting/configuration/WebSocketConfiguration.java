package com.example.virtual.meeting.configuration;

import com.example.virtual.meeting.component.wshandler.MeetingEndpointHandler;
import com.example.virtual.meeting.component.wshandler.WebSocketTestEndpointHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private WebSocketTestEndpointHandler webSocketTestEndpointHandler;

    @Autowired
    private MeetingEndpointHandler meetingEndpointHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webSocketTestEndpointHandler, "/ws")
                .addInterceptors(new HttpSessionHandshakeInterceptor());

        webSocketHandlerRegistry.addHandler(meetingEndpointHandler, "/vmr")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}
