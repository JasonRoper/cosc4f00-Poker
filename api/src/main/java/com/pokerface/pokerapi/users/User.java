package com.pokerface.pokerapi.users;


import javax.persistence.*;

/**
 * User is the database representation of the users permanent representation.
 * When they log in, this is the data they need to be able to validate and then be able to access
 */
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
    private ConnectionStatus connection;

    private String role;
    private int money;

    public User() {
    }

    /**
     * This constructor is what is used upon valid registration to create a user.
     * @param username string of their username
     * @param securePassword string of their password
     * @param email string of their email
     */
    public User(String username, String securePassword, String email) {
        this.username = username;
        this.password = securePassword;
        this.email = email;
        this.role = "ROLE_USER";
        this.connection = ConnectionStatus.DISCONNECTED;
        this.money = 0;
    }

    /**
     * This bundles up the user info to transmit when needed to the Front End
     * @return a UserTransport object
     */
    public UserTransport toTransfer() {
        return new UserTransport(this);
    }


    /**
     * Accesses the user's role, if they are a user or admin
     * @return String representing their role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the users role
     * @param role the role to set for them
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Returns the users id
     * @return a long value representing their ID in the Database
     */
    public long getId() {
        return id;
    }

    /**
     * Accesses the users username
     * @return the username in question as a string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Changes the users username
     * @param username the username to change their users username to
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the users password
     * @return the string representation of their password
     */
    public String getPassword() {
        return password;
    }

    /**
     * setPassword sets the users password
     * @param password the password to be updated to
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the users email
     * @return string representing the users email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setEmail sets their email
     * @param email the string email to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * The users ranking, used for competetive matchmaking
     * @return int representing rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * sets the users rating
     * @param rating the int to set their rating to
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    public ConnectionStatus getConnection() {
        return connection;
    }

    public void setConnection(ConnectionStatus connection) {
        this.connection = connection;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public enum ConnectionStatus {
        DISCONNECTED, CONNECTED
    }

}