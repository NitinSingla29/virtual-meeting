package com.example.virtual.meeting.repository;

import com.example.virtual.meeting.domain.MeetingRoom;
import org.springframework.stereotype.Repository;

public interface MeetingRoomRepository {

    MeetingRoom getMeetingRoom(int roomNumber);

    MeetingRoom getMeetingRoom(String webSocketSessionId);

    void addAssociation(MeetingRoom meetingRoom, String webSocketSessionId);

    void addAssociation(int roomNumber, String webSocketSessionId);
}
