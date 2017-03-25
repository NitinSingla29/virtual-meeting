package com.example.virtual.meeting.dto;

public class UserSession {

    private String userName;
    private String sessionId;

    public UserSession(final String userName, final String sessionId) {
        this.userName = userName;
        this.sessionId = sessionId;
    }

    public UserSession() {
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

    @Override
    public boolean equals(final Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        final UserSession that = (UserSession) o;

        return getSessionId() != null ? getSessionId().equals(that.getSessionId()) : that.getSessionId() == null;
    }

    @Override
    public int hashCode() {
        return getSessionId() != null ? getSessionId().hashCode() : 0;
    }
}
