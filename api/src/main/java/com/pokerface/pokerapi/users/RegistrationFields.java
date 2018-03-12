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

    /**
     * The registrations field, takes the things necessary to register a user
     * @param username a string, their username of the new user
     * @param password a string, the password of the new user
     * @param email a string, the email of the new user
     */
    public RegistrationFields(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }


    /**
     * Returns the username
     * @return string username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     * @param username string username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * reutnrs the password
     * @return string of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set the password
     * @param password string password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * gets the email
     * @return string representing email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the registration
     * @param email the string to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
