package com.example.virtual.meeting.domain;

import com.example.virtual.meeting.dto.UserSession;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MeetingRoom {

    private final int roomNumber;
    private final String roomName;
    private final Set<UserSession> userSessions;

    public MeetingRoom(int roomNumber, String roomName) {
        this.roomNumber = roomNumber;
        this.roomName = roomName;
        this.userSessions = new HashSet<>();
    }

    public MeetingRoom(int roomNumber) {
        this(roomNumber, null);
    }

    public String getRoomName() {
        return roomName;
    }

    public Set<UserSession> getUserSessions() {
        return Collections.unmodifiableSet(userSessions);
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

    public void addSession(UserSession userSession) {
        this.userSessions.add(userSession);
    }
}
