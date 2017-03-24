package com.example.virtual.meeting.dto;

public class MeetingRoomRequest extends BaseRequest {
    private int roomNumber;
    private String name;
    private String userName;

    public MeetingRoomRequest(final int roomNumber, final String name, final String userName) {
        this.roomNumber = roomNumber;
        this.name = name;
        this.userName = userName;
    }

    public MeetingRoomRequest() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
