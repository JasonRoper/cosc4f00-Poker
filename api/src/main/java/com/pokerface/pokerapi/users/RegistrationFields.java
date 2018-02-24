package com.pokerface.pokerapi.users;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * {@link RegistrationFields} contains all the information
 * that a client will use to create a new user account.
 */
public class RegistrationFields {
    @NotNull()
    @Size(min = UserConstants.USERNAME_MIN_CHARACTER_LENGTH,
            max = UserConstants.USERNAME_MAX_CHARACTER_LENGTH)
    private String username;

    @NotNull
    @Size(min = UserConstants.PASSWORD_MIN_LENGTH)
    private String password;

    @NotEmpty
    @Email
    private String email;

    public RegistrationFields(){
    }

    public RegistrationFields(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }


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
