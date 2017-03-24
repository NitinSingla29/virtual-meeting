package com.example.virtual.meeting.main;

import com.example.virtual.meeting.configuration.ApplicationConfiguration;
import com.example.virtual.meeting.model.RequestType;
import com.example.virtual.meeting.model.MeetingRoomRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApplicationConfiguration.class)
public class Application {
    public static void main(String[] args) throws Exception {

//        final MeetingRoomRequest meetingRoomRequest = new MeetingRoomRequest(RequestType.JOIN_ROOM, 1, "Hi");
//        System.out.println(new ObjectMapper().writeValueAsString(meetingRoomRequest));
        SpringApplication.run(Application.class, args);
    }
}