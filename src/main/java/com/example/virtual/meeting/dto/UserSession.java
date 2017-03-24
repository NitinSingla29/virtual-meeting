package com.example.virtual.meeting.dto;

public class UserSession {

    private String userName;
    private String sessionId;

    public UserSession(final String userName, final String sessionId) {
        this.userName = userName;
        this.sessionId = sessionId;
    }

    public UserSession(final String userName) {
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }
}
