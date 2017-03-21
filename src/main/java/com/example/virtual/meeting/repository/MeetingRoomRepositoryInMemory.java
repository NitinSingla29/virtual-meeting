package com.example.virtual.meeting.repository;

import com.example.virtual.meeting.domain.MeetingRoom;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NSingla on 26-02-2017.
 */
public class MeetingRoomRepositoryInMemory implements MeetingRoomRepository {

    private Map<Integer, MeetingRoom> meetingRoomsMap = new HashMap<Integer, MeetingRoom>();

    private Map<String, MeetingRoom> sessionToRoomMap = new HashMap<>();

    @Override
    public void addAssociation(MeetingRoom meetingRoom, String webSocketSessionId) {
        MeetingRoom existingRoom = meetingRoomsMap.get(meetingRoom.getRoomNumber());
        if (existingRoom == null) {
            meetingRoomsMap.put(meetingRoom.getRoomNumber(), meetingRoom);
            existingRoom = meetingRoom;
        }
        addSession(webSocketSessionId, existingRoom);
    }

    @Override
    public void addAssociation(int roomNumber, String webSocketSessionId) {
        MeetingRoom existingRoom = meetingRoomsMap.get(roomNumber);
        if (existingRoom != null) {
            addSession(webSocketSessionId, existingRoom);
        }
        throw new IllegalArgumentException("No meeting room exist for room number=" + roomNumber);
    }

    private void addSession(String webSocketSessionId, MeetingRoom existingRoom) {
        existingRoom.addSession(webSocketSessionId);
        sessionToRoomMap.put(webSocketSessionId, existingRoom);
    }

    @Override
    public MeetingRoom getMeetingRoom(int roomNumber) {
        return this.meetingRoomsMap.get(roomNumber);
    }

    @Override
    public MeetingRoom getMeetingRoom(String webSocketSessionId) {
        return this.sessionToRoomMap.get(webSocketSessionId);
    }

}