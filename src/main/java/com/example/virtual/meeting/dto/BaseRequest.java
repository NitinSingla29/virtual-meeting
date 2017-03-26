package com.example.virtual.meeting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseRequest {

    private String type;

    private String getType() {
        return type;
    }

    public RequestType getRequestType() {
        return RequestType.valueOf(this.type.toUpperCase());
    }

    public void setType(final String type) {
        this.type = type;
    }
}
