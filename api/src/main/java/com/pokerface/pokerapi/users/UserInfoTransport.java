package com.pokerface.pokerapi.users;

/**
 * UserInfoTransport is a user facing object that will deliver
 * information that is ok for a standard user to view.
 *
 * DO NOT put anything that should only be seen by the user who
 * owns the profile here.
 */
public class UserInfoTransport {
    private long id;
    private String username;

    public UserInfoTransport(User user) {
        this.username = user.getUsername();
        this.id = user.getId();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}