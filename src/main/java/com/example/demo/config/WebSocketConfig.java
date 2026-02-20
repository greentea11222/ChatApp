package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //WebSockerメッセージの仲介役を有効にする
//WebSocketの接続エンドポイントなどの設定
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		//サーバーからクライアントへメッセージを送る際の「宛先」の接頭辞
		config.enableSimpleBroker("/topic");
		//クライアントからサーバーへメッセージを送る際の「宛先」の接頭辞
		config.setApplicationDestinationPrefixes("/app");
	}
}
