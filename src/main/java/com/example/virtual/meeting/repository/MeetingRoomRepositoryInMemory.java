package com.example.virtual.meeting.repository;

import com.example.virtual.meeting.domain.MeetingRoom;
import com.example.virtual.meeting.dto.UserSession;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MeetingRoomRepositoryInMemory implements MeetingRoomRepository {

    private Map<Integer, MeetingRoom> meetingRoomsMap = new HashMap<Integer, MeetingRoom>();

    private Map<String, MeetingRoom> sessionToRoomMap = new HashMap<>();

    @Override
    public void addAssociation(MeetingRoom meetingRoom, String userName, String webSocketSessionId) {
        MeetingRoom existingRoom = meetingRoomsMap.get(meetingRoom.getRoomNumber());
        if (existingRoom == null) {
            meetingRoomsMap.put(meetingRoom.getRoomNumber(), meetingRoom);
            existingRoom = meetingRoom;
        }
        addSession(userName, webSocketSessionId, existingRoom);
    }

    @Override
    public void addAssociation(int roomNumber, String userName, String webSocketSessionId) {
        MeetingRoom existingRoom = meetingRoomsMap.get(roomNumber);
        if (existingRoom != null) {
            addSession(userName, webSocketSessionId, existingRoom);
        }
        throw new IllegalArgumentException("No meeting room exist for room number=" + roomNumber);
    }

    @Override
    public void removeRoomAssociation(final String userName, final String webSocketSessionId) {
        this.removeRoomAssociation(new UserSession(userName, webSocketSessionId));
    }

    @Override
    public void removeRoomAssociation(final String webSocketSessionId) {
        this.removeRoomAssociation(new UserSession("", webSocketSessionId));
    }

    private void removeRoomAssociation(UserSession userSession) {
        final MeetingRoom meetingRoom = this.sessionToRoomMap.get(userSession.getSessionId());
        if(meetingRoom != null) {
            meetingRoom.removeSession(userSession);
        }
    }

    private void addSession(String userName, String webSocketSessionId, MeetingRoom existingRoom) {
        existingRoom.addSession(new UserSession(userName,webSocketSessionId));
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
