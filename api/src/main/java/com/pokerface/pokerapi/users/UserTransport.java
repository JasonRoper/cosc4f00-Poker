package com.pokerface.pokerapi.users;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

/**
 * UserTransport is a user facing object that will deliver
 * profile information to the owner of the profile.
 *
 * TODO: should user config updates be sent via this object? or another one?
 */
public class UserTransport {
    private long id;
    private String username;
    private String email;

    /**
     * This constructor takes an ID, a UserName and an Email to generate a UserTransport
     * @param id long for the userID
     * @param username string for their username
     * @param email string for their email
     */
    public UserTransport(long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserTransport(){}

    /**
     * Returns the username
     * @return String of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets a username
     * @param username String representing username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Returns the email
     * @return a String of the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email given
     * @param email String of the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the userID
     * @return long representing the ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID
     * @param id long representing the ID of the user
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * An ovverride for equals, to confirm two user transports are equivalent
     * @param o is the object being compared
     * @return whether or not these objects are requivalent
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTransport that = (UserTransport) o;

        if (id != that.id) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return email != null ? email.equals(that.email) : that.email == null;
    }

    /**
     * Used for comparison sake
     * @return the int value of the hash
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
