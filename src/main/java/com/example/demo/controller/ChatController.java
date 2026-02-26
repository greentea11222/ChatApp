package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ChatMessageService;

@Controller
//チャット画面全体の表示
public class ChatController {
	
	private ChatMessageService chatMessageService = null;
	
	private UserRepository userRepository = null;
	
	//コンストラクタ
	public ChatController(ChatMessageService chatMessageService, UserRepository userRepository) {
		this.chatMessageService = chatMessageService;
		this.userRepository = userRepository;
	}
	
	//チャット画面の表示
	@GetMapping("/chat")
	public String chatPage() {
		return "chat";
	}
	
	//クライアントが/app/messageに送ったメッセージを受け取る
	@MessageMapping("/message")
	//処理した結果を/topic/messagesを購読（待機）している全員に送る
	@SendTo("/topic/messages")
	public ChatMessage broadcastMessage(String content, Authentication authentication) {
		//ログイン中の名前を取得
		String username = authentication.getName();
		User sender = null;
				//userRepository.findByUsername(username);

		
		//画面に返すオブジェクトを作成
		ChatMessage message = new ChatMessage(content, username, sender);
		message.setContent(content);
		message.setSenderName(username);
		message.setTimestamp(LocalDateTime.now());
		
		return message;
	}
}
