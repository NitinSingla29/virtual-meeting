package com.example.virtual.meeting.configuration;

import com.example.virtual.meeting.controller.HelloWorldConroller;
import com.example.virtual.meeting.wshandler.WebSocketTestEndpointHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = {HelloWorldConroller.class, WebSocketTestEndpointHandler.class})
@Import({WebSocketConfiguration.class})
public class ApplicationConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
}
