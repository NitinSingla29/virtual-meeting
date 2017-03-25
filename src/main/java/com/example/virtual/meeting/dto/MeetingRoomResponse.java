package com.example.virtual.meeting.dto;

import com.example.virtual.meeting.domain.MeetingRoom;

public class MeetingRoomResponse extends BaseResponse {
    private ResponseType type;
    private MeetingRoom meetingRoom;

    public MeetingRoomResponse(final ResponseType type, final MeetingRoom meetingRoom) {
        this.type = type;
        this.meetingRoom = meetingRoom;
    }

    public MeetingRoomResponse() {

    }

    public ResponseType getType() {
        return type;
    }

    public void setType(final ResponseType type) {
        this.type = type;
    }

    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(final MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }
}
