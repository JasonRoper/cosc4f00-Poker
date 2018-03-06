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

@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);


    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserTransport> register(@Valid @RequestBody RegistrationFields fields) {
        return new ResponseEntity<>(userService.register(fields), HttpStatus.CREATED);
    }

    @PutMapping()
    public UserTransport update(@RequestBody UserTransport updatedUser) {
        return userService.updateUser(updatedUser);
    }

    @DeleteMapping()
    public void delete() {
        userService.deleteUser();
    }

    @GetMapping()
    public ListResponse<UserInfoTransport> listing() {
        return new ListResponse<>(userService.listUsers());
    }

    @GetMapping("/login")
    public UserInfoTransport login(Principal principal){
        return userService.getUser(principal.getName());
    }
}
