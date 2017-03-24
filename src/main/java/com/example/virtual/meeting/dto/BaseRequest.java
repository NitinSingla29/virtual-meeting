package com.example.virtual.meeting.dto;

public class BaseRequest {

    private RequestType requestType;

    public BaseRequest(final RequestType requestType) {
        this.requestType = requestType;
    }

    public BaseRequest() {
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(final RequestType requestType) {
        this.requestType = requestType;
    }
}
