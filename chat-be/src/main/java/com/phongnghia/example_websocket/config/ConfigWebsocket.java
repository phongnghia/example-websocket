package com.phongnghia.example_websocket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ConfigWebsocket implements WebSocketMessageBrokerConfigurer {

    @Value("${domain-name}")
    private String DOMAIN_NAME;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue", "/notification");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/example-websocket");
//        registry.addEndpoint("/example-websocket").setAllowedOrigins("http://localhost:63342").withSockJS();
//        registry.addEndpoint("/example-websocket").setAllowedOrigins("http://127.0.0.1:5500").withSockJS();
        registry.addEndpoint("/example-websocket").setAllowedOrigins(String.format("http://%s", DOMAIN_NAME)).withSockJS();
        registry.addEndpoint("/example-websocket").setAllowedOrigins(String.format("https://%s", DOMAIN_NAME)).withSockJS();
    }
}
