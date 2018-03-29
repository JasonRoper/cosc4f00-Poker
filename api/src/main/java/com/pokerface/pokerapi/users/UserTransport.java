package com.pokerface.pokerapi.users;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

/**
 * UserTransport is a user facing object that will deliver
 * profile information to the owner of the profile.
 *
 * TODO: should user config updates be sent via this object? or another one?
 */
public class UserTransport extends UserInfoTransport {
    private String email;

    /**
     * This constructor takes an ID, a UserName and an Email to generate a UserTransport
     */
    public UserTransport(User user) {
        super(user);
        this.email = user.getEmail();
    }

    public UserTransport(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTransport)) return false;
        if (!super.equals(o)) return false;

        UserTransport that = (UserTransport) o;

        return getEmail() != null ? getEmail().equals(that.getEmail()) : that.getEmail() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        return result;
    }
}
