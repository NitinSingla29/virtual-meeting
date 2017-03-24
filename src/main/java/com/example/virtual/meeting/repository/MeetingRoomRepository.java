package com.example.virtual.meeting.repository;

import com.example.virtual.meeting.domain.MeetingRoom;

public interface MeetingRoomRepository {

    MeetingRoom getMeetingRoom(int roomNumber);

    MeetingRoom getMeetingRoom(String webSocketSessionId);

    void addAssociation(MeetingRoom meetingRoom, String userName, String webSocketSessionId);

    void addAssociation(int roomNumber, String userName, String webSocketSessionId);
}
