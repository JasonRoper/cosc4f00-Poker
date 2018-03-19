package com.pokerface.pokerapi.config;

import com.pokerface.pokerapi.users.UserService;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    private UserService users;
    private PasswordEncoder encoder;

    /**
     * The {@link SecurityConfig} depends on the {@link UserService} and
     * the {@link PasswordEncoder} in order to tell Spring Security how to retrieve
     * users, and how to encode the passwords provided by the client.
     * @param users the user service to query for user accounts
     * @param encoder the encoder that will be used to encode passwords to check if a user
     *                is authenticated
     */
    public SecurityConfig(UserService users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
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
                .and().csrf().disable() //csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .cors().disable()
                // enable logging out to invalidate the JSESSION cookie
                .logout().permitAll().logoutUrl("/api/v1/users/logout")
                // we don't want the default authentication popup to show if we are not authenticated
                .and().exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint("FormBased"));
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