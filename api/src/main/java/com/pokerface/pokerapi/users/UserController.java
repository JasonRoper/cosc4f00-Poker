package com.pokerface.pokerapi.users;

import com.pokerface.pokerapi.util.BadRequestError;
import com.pokerface.pokerapi.util.ListResponse;
import com.pokerface.pokerapi.util.RESTError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);


    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public UserTransport register(@RequestBody RegistrationFields fields) throws RESTError {
        return userService.register(fields);
    }

    @PutMapping()
    public UserTransport update(@RequestBody UserTransport updatedUser) throws RESTError {
        return userService.updateUser(updatedUser);
    }

    @DeleteMapping()
    public void delete() {
        userService.deleteUser();
    }

    @GetMapping()
    public ListResponse<UserInfoTransport> listing() {
        return new ListResponse<UserInfoTransport>(userService.listUsers());
    }
}
