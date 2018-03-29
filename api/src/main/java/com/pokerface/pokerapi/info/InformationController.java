package com.pokerface.pokerapi.info;

import com.pokerface.pokerapi.game.GameService;
import com.pokerface.pokerapi.users.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/")
@RestController()
public class InformationController {
    private final GameService gameService;
    private final UserService userService;

    public InformationController(final GameService gameService, final UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("/info")
    public GeneralInformation getGeneralInformation() {
        GeneralInformation information = new GeneralInformation();
        information.setActiveGames(gameService.getNumActiveGames());
        information.setRegisteredUsers(userService.getNumUsers());
        information.setUsersOnline(userService.getNumUsersOnline());
        information.setHighestRated(userService.getUsersByRating(3));
        return information;
    }
}
