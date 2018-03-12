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

    /**
     * Create an new {@link UserInfoTransport} from the given {@link User}
     * @param user the user to create this UserInfoTransport from
     */
    public UserInfoTransport(User user) {
        this.username = user.getUsername();
        this.id = user.getId();
    }

    /**
     * GetUserName returns the username of the object
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets the username of the object
     * @param username String username of the object
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * returns the ID of the user
     * @return the long ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the user id this transport represents
     * @param id long userID
     */
    public void setId(long id) {
        this.id = id;
    }
}
