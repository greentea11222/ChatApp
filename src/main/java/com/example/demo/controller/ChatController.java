package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//チャット画面全体の表示
public class ChatController {
	//チャット画面の表示
	@GetMapping("/chat")
	public String chatPage() {
		return "chat";
	}
	
	//クライアントが/app/messageに送ったメッセージを受け取る
	@MessageMapping("/message")
	//処理した結果を/topic/messagesを購読（待機）している全員に送る
	@SendTo("/topic/messages")
	public String broadcastMessage(String message) {
		//届いた文字列をそのまま横流し
		return message;
	}
}
