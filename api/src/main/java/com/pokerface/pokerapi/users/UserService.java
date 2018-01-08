package com.pokerface.pokerapi.users;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository users;

    public UserService(UserRepository userRepository) {
        this.users = userRepository;
    }

    public Session Login(LoginCredentials credentials) {
        return new Session(0);
    }

    public void Logout(Session session) {

    }
}
