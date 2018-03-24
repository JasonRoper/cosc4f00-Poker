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
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link UserService} operates with the {@link UserController}. {@link UserController} receives messages from users and maps
 * those to {@link UserService} methods in order to apply the logic and changes required to the {@link UserRepository}.
 *
 * {@link UserRepository} exists within {@link UserService}, it is used to save, and pull users as needed by the
 * {@link UserController}.
 */
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * The {@link UserService} constructor requires a repository and an encoder for passwords
     * @param userRepository the {@link UserRepository} where all user data will be stored
     * @param encoder the encoder to keep passwords secure
     */
    public UserService(final UserRepository userRepository, final PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        User admin = new User("admin",
                encoder.encode("admin"),
                "admin@pokerpals.org");
        admin.setRole("ROLE_USER,ROLE_ADMIN");
        User jason = new User("jason",
                encoder.encode("password"),
                "jason@pokerpals.org");
        admin.setRole("ROLE_USER,ROLE_ADMIN");
        User adam = new User("adam", encoder.encode("password"),"adam@pokerpals.org");
        User javon = new User("javon", encoder.encode("password"),"javon@pokerpals.org");
        User ashley = new User("ashley", encoder.encode("password"),"ashley@pokerpals.org");
        jason.setRole("ROLE_USER,ROLE_ADMIN");
        javon.setRole("ROLE_USER,ROLE_ADMIN");
        ashley.setRole("ROLE_USER,ROLE_ADMIN");
        adam.setRole("ROLE_USER,ROLE_ADMIN");
        this.userRepository.save(jason);
        this.userRepository.save(admin);
        this.userRepository.save(adam);
        this.userRepository.save(javon);
        this.userRepository.save(ashley);
    }

    /**
     * Takes a registration and tries to create the user, throws exceptions if information is faulty
     * @param fields the fields of registration, username password email
     * @return a {@link UserTransport} representing the user created
     */
    public UserTransport register(RegistrationFields fields) {

        if (userRepository.existsByEmailIgnoreCase(fields.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        if (userRepository.existsByUsernameIgnoreCase(fields.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        User newUser = new User(fields.getUsername(),
                encoder.encode(fields.getPassword()),
                fields.getEmail());

        return userRepository.save(newUser).toTransfer();
    }

    /**
     * Takes a username and pulls up all the details of that user, to facilitate Spring Security
     * @param username the user being pulled
     * @return a UserDetails object
     * @throws UsernameNotFoundException throws this exception if the UserName does not pull up a User
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole());

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(), auth);
    }

    /**
     * Creates a {@link UserTransport}, representing the user updated with the information
     * @param updatedUser the user information being updated
     * @return a UserTransport representing the updated user
     */
    public UserTransport updateUser(UserTransport updatedUser) {
        //TODO: switch to using UserUpdateTransport
        User user = userRepository.findByUsername(updatedUser.getUsername());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        userRepository.save(user);
        return updatedUser;
    }

    /**
     * Deletes the users from the repository
     */
    public void deleteUser() {
        //TODO: not implemented
    }

    /**
     * listUsers returns a list of every user in the {@link UserRepository}
     * @return a List object containing {@link UserInfoTransport}s of all users
     */
    public List<UserInfoTransport> listUsers() {
        ArrayList<UserInfoTransport> users = new ArrayList<>();
        for(User user : userRepository.findAll()) {
            users.add(new UserInfoTransport(user));
        }
        return users;
    }

    /**
     * getUserByUsername gets a UserInfoTransport by using a String of a username
     * @param username the String of the username being searched
     * @return UserInfoTransport of the user found
     */
    public UserInfoTransport getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new UserInfoTransport(user);
        } else {
            return null;
        }
    }

    public UserInfoTransport getUser(long id) {
        User user = userRepository.findOne(id);
        if (user != null) {
            return new UserInfoTransport(user);
        } else {
            return null;
        }
    }

    public int applyRatingChange( long userID,int ratingChange){
        User user = userRepository.findOne(userID);
        user.setRating(user.getRating()+ratingChange);
        userRepository.save(user);
        return user.getRating();
    }
}
