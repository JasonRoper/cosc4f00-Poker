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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTransport that = (UserTransport) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return email != null ? email.equals(that.email) : that.email == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
