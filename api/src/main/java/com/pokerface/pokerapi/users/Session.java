package com.pokerface.pokerapi.users;

public class Session {
    // Dummy class until we figure out spring security
    private long sessionId;

    public Session(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getSessionId() {
        return sessionId;
    }
}
