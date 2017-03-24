package com.example.virtual.meeting.dto;

import com.example.virtual.meeting.domain.MeetingRoom;

public class MeetingRoomResponse extends BaseResponse {
    private RequestType requestType;
    private MeetingRoom meetingRoom;

    public MeetingRoomResponse(final RequestType requestType, final MeetingRoom meetingRoom) {
        this.requestType = requestType;
        this.meetingRoom = meetingRoom;
    }

    public MeetingRoomResponse() {

    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(final RequestType requestType) {
        this.requestType = requestType;
    }

    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(final MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }
}
