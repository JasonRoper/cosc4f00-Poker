package com.pokerface.pokerapi.users;

import com.pokerface.pokerapi.util.BadRequestError;
import com.pokerface.pokerapi.util.RESTError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(final UserRepository userRepository, final PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        User admin = new User("admin",
                encoder.encode("admin"),
                "admin@pokerpals.org");
        admin.setRole("ROLE_USER,ROLE_ADMIN");
        this.userRepository.save(admin);
    }

    public UserTransport register(@Valid RegistrationFields fields) throws BadRequestError {
        BadRequestError errors = new BadRequestError("Registration fields are invalid");

        if (userRepository.existsByEmailIgnoreCase(fields.getEmail())) {
            errors.addFieldError("email", "email already exists");
        }

        if (userRepository.existsByUsernameIgnoreCase(fields.getUsername())) {
            errors.addFieldError("username", "username already exists");
        }

        if (errors.getNumberErrors() > 0) throw errors;

        User newUser = new User(fields.getUsername(),
                encoder.encode(fields.getPassword()),
                fields.getEmail());

        return userRepository.save(newUser).toTransfer();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(), auth);
    }

    public UserTransport updateUser(UserTransport updatedUser) {
        //TODO: switch to using UserUpdateTransport
        User user = userRepository.findByUsername(updatedUser.getUsername());
        user.setUsername(updatedUser.getUsername());
        user.setPassword(encoder.encode(updatedUser.getPassword()));
        user.setEmail(updatedUser.getEmail());
        userRepository.save(user);
        return updatedUser;
    }

    public void deleteUser() {
        //TODO: not implemented
    }

    public List<UserInfoTransport> listUsers() {
        ArrayList<UserInfoTransport> users = new ArrayList<>();
        for(User user : userRepository.findAll()) {
            users.add(new UserInfoTransport(user));
        }
        return users;
    }

    @ExceptionHandler(RESTError.class)
    public RESTError basicHandler(RESTError error) {
        return error;
    }
}
