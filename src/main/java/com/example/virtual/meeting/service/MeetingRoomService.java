package com.example.virtual.meeting.service;

import com.example.virtual.meeting.domain.MeetingRoom;
import com.example.virtual.meeting.dto.MeetingRoomRequest;
import com.example.virtual.meeting.dto.UserSession;
import com.example.virtual.meeting.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MeetingRoomService {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    private AtomicInteger roomCounter = new AtomicInteger();

    public MeetingRoom joinMeetingRoon(MeetingRoomRequest meetingRoomRequest, String webSocketSessionId) {
        MeetingRoom meetingRoom = this.createOrGetRoom(meetingRoomRequest);
        this.meetingRoomRepository.addAssociation(meetingRoom, meetingRoomRequest.getUserName(),
                webSocketSessionId);
        return meetingRoom;
    }

    private MeetingRoom createOrGetRoom(final MeetingRoomRequest meetingRoomRequest) {
        MeetingRoom meetingRoom = meetingRoomRepository.getMeetingRoom(meetingRoomRequest.getRoomNumber());
        if(meetingRoom == null) {
            int roomNumber = meetingRoomRequest.getRoomNumber();
            roomNumber = roomNumber == 0 ? roomCounter.incrementAndGet() : roomNumber;
            final String roomName = meetingRoomRequest.getName() != null ? meetingRoom.getRoomName() : "MeetingRomm" +
                    roomNumber;
            meetingRoom = new MeetingRoom(roomNumber, roomName);
        }
        return meetingRoom;
    }

    public MeetingRoom leaveMeetingRoom(final String sessionId) throws IOException {
        final MeetingRoom meetingRoom = this.meetingRoomRepository.getMeetingRoom(sessionId);
        meetingRoom.removeSession(new UserSession("", sessionId));
        return meetingRoom;
    }

    public MeetingRoom getMeetingRoom(String sessionId) {
        return this.meetingRoomRepository.getMeetingRoom(sessionId);
    }
}


