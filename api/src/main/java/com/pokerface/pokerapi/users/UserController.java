package com.pokerface.pokerapi.users;

import com.pokerface.pokerapi.util.ListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * UserController exists to map the communication from the Front End to the UserService where appropriate.
 *
 * This handles the communication of log ins, and registration, ensuring to communicate proper Transport objects to the user.
 */
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * UserController requires a userService object to be created, this ensures it is instantiated
     * @param userService the userService that UserController will use
     */
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * register handles the mapping of the registration of a user, taking the fields they've submitted and passing them along to UserService to parse
     * @param fields the fields of the registration
     * @return the response, if it was successful or not
     */
    @PostMapping()
    public ResponseEntity<UserTransport> register(@Valid @RequestBody RegistrationFields fields) {
        return new ResponseEntity<>(userService.register(fields), HttpStatus.CREATED);
    }

    /**
     * update takes an updated user, and hands it to GameService for processing
     * @param updatedUser the updatedUser object information
     * @return a transport of the new user
     */
    @PutMapping()
    public UserTransport update(@RequestBody UserTransport updatedUser) {
        return userService.updateUser(updatedUser);
    }

    /**
     * Deletes a user mapped by the path required
     */
    @DeleteMapping()
    public void delete() {
        userService.deleteUser();
    }

    /**
     * Lists users.
     * @return a list of users
     */
    @GetMapping()
    public ListResponse<UserInfoTransport> listing() {
        return new ListResponse<>(userService.listUsers());
    }

    /**
     * Attempts login via mapping
     * @param principal is the user logging in
     * @return returns the successful login.
     */
    @GetMapping("/login")
    public UserInfoTransport login(Principal principal){
        return userService.getUser(principal.getName());
    }
}
