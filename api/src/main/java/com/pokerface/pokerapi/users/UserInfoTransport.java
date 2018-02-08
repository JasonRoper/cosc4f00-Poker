package com.pokerface.pokerapi.users;

public class UserInfoTransport {
    private String username;

    public UserInfoTransport(User user) {
        this.username = user.getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
