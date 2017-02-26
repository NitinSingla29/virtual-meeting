package com.example.virtual.meeting.configuration;

import com.example.virtual.meeting.controller.HelloWorldConroller;
import com.example.virtual.meeting.wshandler.MeetingEndpointHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = {HelloWorldConroller.class, MeetingEndpointHandler.class})
@Import({WebSocketConfiguration.class})
public class ApplicationConfiguration {
}
