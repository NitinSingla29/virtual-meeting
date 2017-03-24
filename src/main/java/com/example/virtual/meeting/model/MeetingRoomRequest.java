package com.example.virtual.meeting.model;

public class MeetingRoomRequest extends BaseRequest {
    private int roomNumber;
    private String name;

    public MeetingRoomRequest(RequestType requestType, int roomNumber, String message, final String name) {
        this.roomNumber = roomNumber;
        this.name = name;
    }

    public MeetingRoomRequest() {
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
