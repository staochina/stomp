package com.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        logger.debug("configureMessageBroker()");
        //基于内存的STOMP消息代理
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app").setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        logger.debug("registerStompEndpoints()");
        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*").withSockJS();
    }



}
