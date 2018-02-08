package com.pokerface.pokerapi.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private UserRepository users;

    private PasswordEncoder encoder;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.users = userRepository;
        this.encoder = encoder;
        User admin = new User("admin",
                encoder.encode("admin"),
                "jkmroper@gmail.com");
        admin.setRole("USER_ROLE,ADMIN_ROLE");
        users.save(admin);
    }

    public UserTransport register(RegistrationFields fields) {
        User exists = users.findByUsernameIgnoreCaseOrEmailIgnoreCase(fields.getUsername(),fields.getEmail());
        if (exists != null) return null;

        User newUser = new User(fields.getUsername(),
                encoder.encode(fields.getPassword()),
                fields.getEmail());
        return users.save(newUser).toDTO();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.findByUsername(username);
        logger.debug("loading username: " + user.getUsername());

        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(), auth);
    }

    public UserTransport updateUser(UserTransport updatedUser) {
        User user = users.findByUsername(updatedUser.getUsername());
        user.setUsername(updatedUser.getUsername());
        user.setPassword(encoder.encode(updatedUser.getPassword()));
        user.setEmail(updatedUser.getEmail());
        users.save(user);
        return updatedUser;
    }

    public void deleteUser() {
        //TODO: not implemented
    }
}
