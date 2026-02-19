package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //WebSockerメッセージの仲介役を有効にする
//WebSocketの接続エンドポイントなどの設定
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//	@Override
//	public void configureMessageBroker(MessageBrokerRegistry config) {
}
