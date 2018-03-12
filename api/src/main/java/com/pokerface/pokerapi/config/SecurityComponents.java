package com.pokerface.pokerapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * {@link SecurityComponents} provides beans required to facilitate security.
 * <p>
 *     this class should only be used by the Spring injection framework. Creating this manually is a mistake.
 */
@Configuration
public class SecurityComponents {
    /**
     * Provides a {@link PasswordEncoder} to the {@link com.pokerface.pokerapi.users.UserService} and
     * to Spring Security. We are  currently using BCrypt to hash our passwords.
     *
     * @return the {@link PasswordEncoder} that is to be used
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
