package com.pokerface.pokerapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * {@link WebMvcConfig} configures how requests are handled.
 * <p>
 * by extending {@link WebMvcConfigurerAdapter} we can override methods to configure how web requests are
 * handled. Currently this is being used to allow CORS (Cross Origin Resource Sharing) for the development
 * server.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    /**
     * {@code addCorsMappings} is used to add paths that are allowed to be requested from another origin.
     * <p>
     * currently any request from http://localhost:8081 (the front end dev server) is permitted.
     *
     * @param registry the registry to configure.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**").allowedOrigins("http://localhost:8081").allowedHeaders("*");
    }
}
