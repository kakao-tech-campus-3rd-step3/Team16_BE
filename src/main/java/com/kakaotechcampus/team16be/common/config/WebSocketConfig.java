package com.kakaotechcampus.team16be.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //WebSocket의 메세지 처리를 활성화합니다.(카카오톡의 "실시간 메시지 기능 ON" 스위치를 켜는 것)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) { //메세지 브로커를 설정하는 메소드
        config.enableSimpleBroker("/topic", "/queue"); // 구독자에게 전송되는 경로
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 보내는 메시지 경로 접두사
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //STOMP 접속 지점을 등록하는 메소드
        registry.addEndpoint("/ws") // 클라이언트 접속 URL
                .setAllowedOriginPatterns("*") // CORS 허용(와일드 카드)
                .withSockJS();
    }
}
