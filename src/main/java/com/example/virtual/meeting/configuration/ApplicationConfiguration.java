package com.example.virtual.meeting.configuration;

import com.example.virtual.meeting.component.JsonSerializer;
import com.example.virtual.meeting.component.wshandler.WebSocketTestEndpointHandler;
import com.example.virtual.meeting.controller.HelloWorldConroller;
import com.example.virtual.meeting.repository.MeetingRoomRepositoryInMemory;
import com.example.virtual.meeting.service.MeetingRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = {HelloWorldConroller.class, WebSocketTestEndpointHandler.class,
        MeetingRoomRepositoryInMemory.class, MeetingRoomService.class, JsonSerializer.class})
@Import({WebSocketConfiguration.class})
public class ApplicationConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
}
