package com.pokerface.pokerapi.users;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

/**
 * UserUpdateTransport is an object conveying the new user information after registration is done but information changes
 */
public class UserUpdateTransport {

    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
