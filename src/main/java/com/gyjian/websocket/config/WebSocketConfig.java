package com.gyjian.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @EnableWebSocketMessageBroker用于启用我们的WebSocket服务器
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // withSockJS()是用来为不支持websocket的浏览器启用后备选项，使用了SockJS
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //以“/app”开头的消息应该路由到消息处理方法
        registry.setApplicationDestinationPrefixes("/app");

        //使用内存中的消息代理。以“/topic”开头的消息应该路由到消息代理。消息代理向订阅特定主题的所有连接客户端广播消息
        //registry.enableSimpleBroker("/topic");

        // （可选：使用 RabbitMQ / ActiveMQ 作为消息代理）
        /** Use this for enabling a Full featured broker like RabbitMQ */
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost("192.168.1.70")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");

    }
}
