package com.pokerface.pokerapi.users;

public class UserTransport {
    private String username;
    private String password;
    private String email;

    public UserTransport(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserTransport(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
