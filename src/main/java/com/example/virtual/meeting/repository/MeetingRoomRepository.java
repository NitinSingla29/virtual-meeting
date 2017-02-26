package com.example.virtual.meeting.repository;

import com.example.virtual.meeting.domain.MeetingRoom;

/**
 * Created by NSingla on 26-02-2017.
 */
public interface MeetingRoomRepository {

    MeetingRoom getMeetingRoom(int roomNumber);

    MeetingRoom getMeetingRoom(String webSocketSessionId);

    void addAssociation(MeetingRoom meetingRoom, String webSocketSessionId);

    void addAssociation(int roomNumber, String webSocketSessionId);
}
