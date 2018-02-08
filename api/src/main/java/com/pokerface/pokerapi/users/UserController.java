package com.pokerface.pokerapi.users;

import com.pokerface.pokerapi.util.RESTError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public UserTransport register(@RequestBody RegistrationFields fields) throws RESTError {
        UserTransport user = userService.register(fields);
        if (user == null) {
            throw new RESTError(HttpStatus.BAD_REQUEST, "yo fucked up");
        }
        return user;
    }

    @PutMapping()
    public UserTransport update(@RequestBody UserTransport updatedUser) throws RESTError {
        UserTransport user = userService.updateUser(updatedUser);
        if (user == null) {
            throw new RESTError(HttpStatus.BAD_REQUEST, "yo fucked up");
        }
        return user;
    }

    @DeleteMapping()
    public void delete() {
        userService.deleteUser();
    }
}
