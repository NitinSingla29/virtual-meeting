package com.example.virtual.meeting.model;

public class MeetingRoomRequest {
    private MeetingRequestType meetingRequestType;
    private int roomNumber;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MeetingRoomRequest() {
    }


    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public MeetingRequestType getMeetingRequestType() {
        return meetingRequestType;
    }

    public void setMeetingRequestType(MeetingRequestType meetingRequestType) {
        this.meetingRequestType = meetingRequestType;
    }
}
