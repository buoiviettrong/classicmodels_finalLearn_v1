package com.nixagh.classicmodels.config.web_socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final String[] STOMP_ENDPOINT = {
            "/ws",          // normal endpoint
            "/admin/ws",    // admin endpoint
            "/user/ws"      // user endpoint
    };

    private final String[] MESSAGE_BROKER = {
            "/topic",               // normal topic
            "/admin/notification",   // admin topic
            "/user/notification"
    };

    private final String[] DESTINATION_PREFIX = {
            "/app",         // normal destination prefix
            "/admin/app",    // admin destination prefix
            "/user/app"
    };

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // register the endpoint and enable SockJS
        registry.addEndpoint(STOMP_ENDPOINT)
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // endpoint for sending message
        registry.setApplicationDestinationPrefixes(DESTINATION_PREFIX);
        // endpoint for subscribing to topic
        registry.enableSimpleBroker(MESSAGE_BROKER);
        // normal user destination prefix
//        String USER_DESTINATION_PREFIX = "/user";
//        registry.setUserDestinationPrefix(USER_DESTINATION_PREFIX);
    }
}
