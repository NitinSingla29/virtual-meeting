package com.example.virtual.meeting.dto;

import java.util.List;

public class BaseResponse {
    private boolean error;

    private List<String> errors;

    public BaseResponse() {
    }

    public BaseResponse(final boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public void setError(final boolean error) {
        this.error = error;
    }
}
