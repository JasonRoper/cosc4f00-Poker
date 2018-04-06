package com.pokerface.pokerapi.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class LoadAdministrators {
    private final UserService userService;
    private static Logger logger = LoggerFactory.getLogger(LoadAdministrators.class);

    public LoadAdministrators(final UserService userService) {
        this.userService = userService;
        logger.info("adding test administrators...");
        try {
            this.userService.registerAdministrator(
                    new RegistrationFields("jason", "password", "jason@pokerpals.ca"));
            this.userService.registerAdministrator(
                    new RegistrationFields("javon", "password", "javon@pokerpals.ca"));
            this.userService.registerAdministrator(
                    new RegistrationFields("ashely", "password", "ashely@pokerpals.ca"));
            this.userService.registerAdministrator(
                    new RegistrationFields("adam", "password", "adam@pokerpals.ca"));
        } catch (Exception e) {
            logger.error("failed to create test administrators");
        }
    }
}
