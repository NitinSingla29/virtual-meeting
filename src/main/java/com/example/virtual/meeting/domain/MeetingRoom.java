package com.example.virtual.meeting.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by NSingla on 26-02-2017.
 */
public class MeetingRoom {

    private final int roomNumber;
    private final String roomName;
    private final Set<String> webSocketSessionIds;

    public MeetingRoom(int roomNumber, String roomName) {
        this.roomNumber = roomNumber;
        this.roomName = roomName;
        this.webSocketSessionIds = new HashSet<String>();
    }

    public MeetingRoom(int roomNumber) {
        this(roomNumber, null);
    }

    public String getRoomName() {
        return roomName;
    }

    public Set<String> getWebSocketSessionIds() {
        return Collections.unmodifiableSet(webSocketSessionIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeetingRoom that = (MeetingRoom) o;

        return getRoomNumber() == that.getRoomNumber();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MeetingRoom{");
        sb.append("roomNumber=").append(roomNumber);
        sb.append(", roomName='").append(roomName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return getRoomNumber();
    }

    public int getRoomNumber() {

        return roomNumber;
    }

    public void addSession(String webSocketSessionId) {
        this.webSocketSessionIds.add(webSocketSessionId);
    }
}
