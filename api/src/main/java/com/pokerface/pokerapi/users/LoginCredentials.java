package com.pokerface.pokerapi.users;

public class LoginCredentials {
    private String username;

    public LoginCredentials(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
