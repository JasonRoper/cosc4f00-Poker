package com.pokerface.pokerapi.config;

import com.pokerface.pokerapi.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * The {@link SecurityConfig} handles which paths are accessible with any given authentication
 * level, and tells spring security how to authenticate users.
 * <p>
 * By overriding methods in WebSecurityConfigurerAdapter, we can configure various components of
 * Spring Web Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService users;
    private final PasswordEncoder encoder;
    private final Environment env;
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * The {@link SecurityConfig} depends on the {@link UserService} and
     * the {@link PasswordEncoder} in order to tell Spring Security how to retrieve
     * users, and how to encode the passwords provided by the client.
     * @param users the user service to query for user accounts
     * @param encoder the encoder that will be used to encode passwords to check if a user
     *                is authenticated
     */
    public SecurityConfig(final UserService users, final PasswordEncoder encoder, final Environment env) {
        this.users = users;
        this.encoder = encoder;
        this.env = env;
    }

    /**
     * {@code configure} is is the base method used to secure the website.
     * <p>
     * currently, any request to:
     * <blockquote>
     *     /api/v1/**
     * </blockquote>
     * must have the role {@code USER}
     * <p>
     * and any request to:
     * <blockquote><pre>
     *     POST: /api/v1/users
     *     /**
     *     </pre>
     * </blockquote>
     * is permitted regardless of authentication level
     *
     * @param http the {@link HttpSecurity} context to configure
     * @throws Exception if the configuration fails
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // allow OPTIONS requests in order to permit CORS
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                // allow anyone to register
                .antMatchers(HttpMethod.POST,"/api/v1/users").permitAll()
                // if a user is accessing the api, they must be authenticated
                .antMatchers("/api/v1/**").hasRole("USER")
                // allow anyone to access the front end
                .antMatchers("/**").permitAll()
                // allow authentication with HTTP basic (ie. every request sends a username and password)
                .and().httpBasic()
                // need to use cookie csrf protection without HttpOnly flag in order for it to work with
                // axios and new Websocket()
                .and().cors().disable()
                // enable logging out to invalidate the JSESSION cookie
                .logout().permitAll().logoutUrl("/api/v1/users/logout");

        if (env.getActiveProfiles().length == 0) {
            logger.info("profile is default, disabling csrf");
            http.csrf().disable();
        } else {
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }
    }

    /**
     * Configure the Service used to authenticate users. This configuration attaches {@link UserService}
     * as the authentication provider, and the injected {@link PasswordEncoder} as the encoder used.
     * @param auth the object to configure
     * @throws Exception if the configuration fails
     */
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(users);
        authProvider.setPasswordEncoder(encoder);
    }

}