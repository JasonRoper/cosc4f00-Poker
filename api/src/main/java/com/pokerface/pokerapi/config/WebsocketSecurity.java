package com.pokerface.pokerapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.messaging.Message;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.messaging.access.intercept.ChannelSecurityInterceptor;

@Configuration
public class WebsocketSecurity extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                // allow anyone to connect to the websocket
                .nullDestMatcher().permitAll()
                // and anyone can listen to a message on a websocket
                .simpSubscribeDestMatchers("/messages/**").permitAll()
                // they however need to be a user to listen to the user message
                .simpSubscribeDestMatchers("/user/**").hasRole("USER")
                // but they can't actually send a message
                .simpDestMatchers("/app/**").hasRole("USER")
                // and if they try to do anything else, deny them.
                .anyMessage().denyAll();

    }

    /**
     * Turn off same origin policy for now. otherwise, we get a bunch of csrf errors that will take a bunch of time to
     * fix.
     * @return whether or not same origin protection is disabled or not. (currently disabled)
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }


}
