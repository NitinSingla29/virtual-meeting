package com.example.virtual.meeting.configuration;

import com.example.virtual.meeting.controller.HelloWorldConroller;
import com.example.virtual.meeting.wshandler.WebSocketTestEndpointHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = {HelloWorldConroller.class, WebSocketTestEndpointHandler.class})
@Import({WebSocketConfiguration.class})
public class ApplicationConfiguration {
}
