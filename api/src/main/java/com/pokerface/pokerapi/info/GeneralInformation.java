package com.pokerface.pokerapi.info;

import com.pokerface.pokerapi.game.GameInfoTransport;
import com.pokerface.pokerapi.users.UserInfoTransport;

import java.util.List;

public class GeneralInformation {
    private long registeredUsers;
    private long usersOnline;
    private long activeGames;
    private List<UserInfoTransport> highestRated;

    public GeneralInformation(int registeredUsers, int usersOnline, int activeGames, List<UserInfoTransport> highestRated) {
        this.registeredUsers = registeredUsers;
        this.usersOnline = usersOnline;
        this.activeGames = activeGames;
        this.highestRated = highestRated;
    }

    public GeneralInformation() {
    }

    public long getRegisteredUsers() {
        return registeredUsers;
    }

    public void setRegisteredUsers(long registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    public long getUsersOnline() {
        return usersOnline;
    }

    public void setUsersOnline(long usersOnline) {
        this.usersOnline = usersOnline;
    }

    public long getActiveGames() {
        return activeGames;
    }

    public void setActiveGames(long activeGames) {
        this.activeGames = activeGames;
    }

    public List<UserInfoTransport> getHighestRated() {
        return highestRated;
    }

    public void setHighestRated(List<UserInfoTransport> highestRated) {
        this.highestRated = highestRated;
    }
}
