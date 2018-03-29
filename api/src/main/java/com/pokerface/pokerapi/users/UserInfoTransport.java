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
    private boolean connected;
    private int rating;
    private int money;

    /**
     * Create an new {@link UserInfoTransport} from the given {@link User}
     * @param user the user to create this UserInfoTransport from
     */
    public UserInfoTransport(User user) {
        this.username = user.getUsername();
        this.id = user.getId();
        this.connected = user.getConnection() == User.ConnectionStatus.CONNECTED;
        this.rating = user.getRating();
        this.money = user.getMoney();
    }

    /**
     * Create a new {@link UserInfoTransport} from the given parameters
     *
     * @param id the user id of the {@link User} this {@link UserInfoTransport} represents
     * @param username the username of the {@link User} that this {@link UserInfoTransport} represents
     */
    public UserInfoTransport(long id, String username) {
        this.id = id;
        this.username = username;
        this.connected = false;
    }

    /**
     * Create a new {@link UserInfoTransport} from the given parameters
     *
     * @param id the user id of the {@link User} this {@link UserInfoTransport} represents
     * @param username the username of the {@link User} that this {@link UserInfoTransport} represents
     * @param connected whether or not the {@link User} that this {@link UserInfoTransport} is connected to the server
     */
    public UserInfoTransport(long id, String username, boolean connected) {
        this.id = id;
        this.username = username;
        this.connected = connected;
    }

    /**
     * Create a new {@link UserInfoTransport} with empty values. this is required in order for Jackson to
     * deserialize this class.
     */
    public UserInfoTransport() {
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

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfoTransport)) return false;

        UserInfoTransport that = (UserInfoTransport) o;

        if (getId() != that.getId()) return false;
        if (isConnected() != that.isConnected()) return false;
        if (getRating() != that.getRating()) return false;
        if (getMoney() != that.getMoney()) return false;
        return getUsername() != null ? getUsername().equals(that.getUsername()) : that.getUsername() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        result = 31 * result + (isConnected() ? 1 : 0);
        result = 31 * result + getRating();
        result = 31 * result + getMoney();
        return result;
    }
}
