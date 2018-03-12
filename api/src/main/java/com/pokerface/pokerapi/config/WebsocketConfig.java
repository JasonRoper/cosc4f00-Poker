package com.pokerface.pokerapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * The {@link WebsocketConfig} sets up the websocket, enables the message broker,
 * and assigns a prefix path to be handled by the application.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    /**
     * configure the message broker.
     * <p>
     * assign the {@code /app} prefix to all application handlers, and enable the message broker
     * on {@code /messages }.
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/messages");
    }

    /**
     * Configures the path that the websocket will connect to, and the properties of the connection
     * <p>
     * This sets the websocket connection to be on {@code /live} and that http://localhost:8081 (the
     * dev server) can connect to this path.
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/live").setAllowedOrigins("http://localhost:8081");
    }
}
