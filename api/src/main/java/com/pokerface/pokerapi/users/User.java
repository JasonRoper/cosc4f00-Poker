package com.pokerface.pokerapi.users;


import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private int rating;
    private boolean lookingForGame;
    private LoginState loginState;
    private String role;

    public User() {
    }

    User (String username, String securePassword, String email) {
        this.username = username;
        this.password = securePassword;
        this.email = email;
        this.role = "ROLE_USER";
    }

    public UserTransport toTransfer() {
        return new UserTransport(username, email);
    }

    public LoginState getLoginState() {
        return loginState;
    }

    public void setLoginState(LoginState state) {
        this.loginState = state;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public static enum LoginState {
        LOGGED_IN, HIDDEN, LOGGED_OUT
    }

    public long getId() {
        return id;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isLookingForGame() {
        return lookingForGame;
    }

    public void setLookingForGame(boolean lookingForGame) {
        this.lookingForGame = lookingForGame;
    }
}