package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String chatPage(Model model) {
		//過去のメッセージを古い順に全権取得
		List<ChatMessage> history = chatMessageService.getAllMessages();
		
		//HTMLにhistoryという名前で渡す
		model.addAttribute("history", history);
		
		return "chat";
	}
	
	//クライアントが/app/messageに送ったメッセージを受け取る
	@MessageMapping("/message")
	//処理した結果を/topic/messagesを購読（待機）している全員に送る
	@SendTo("/topic/messages")
	public ChatMessage broadcastMessage(ChatMessage message, Authentication authentication) {
		//ログイン中の名前を取得
		String username = authentication.getName();
		User sender = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
		
		//届いたmessageに、足りない情報をセットする
		message.setSenderName(username);
		message.setSender(sender);
		//ユーザーが登録したアイコンをメッセージに持たせる
		if (sender != null) {
			message.setSenderIcon(sender.getIconData());
		}
		message.setTimestamp(LocalDateTime.now());
		
		//DBに保存
		return chatMessageService.save(message);
	}
	
	@MessageMapping("/delete")
	@SendTo("/topic/messages")
	public ChatMessage deleteMessage(ChatMessage message) {
		//DBから削除
		chatMessageService.deleteById(message.getId());
		
		message.setContent("このメッセージは削除されました。");
		message.setImageData(null);
		message.setDeleteFlg(true);
		return message;
	}
	
	@MessageMapping("/read")
	@SendTo("/topic/messages")
	public ChatMessage markAsRead(ChatMessage message, Authentication authentication) {
		//最新のメッセージをDBから取得
		ChatMessage existingMsg = chatMessageService.findById(message.getId());
		
		//自分の名前を既読リストに追加
		existingMsg.getReadByUsers().add(authentication.getName());
		
		//更新して全員に通知
		return chatMessageService.save(existingMsg);
	}
	
	@MessageMapping("/reaction")
	@SendTo("/topic/messages")
	public ChatMessage handleReaction(ChatMessage message, Authentication authentication) {
		//対象のメッセージをDBから取得
		ChatMessage existingMsg = chatMessageService.findById(message.getId());
		
		//送信者の名前と、送られてきた絵文字（contentフィールドを流用）を取得
		String username = authentication.getName();
		String emoji = message.getContent();
		
		//明日追加！！
		if (emoji.equals(existingMsg.getReactions().get(username))) {
			existingMsg.getReactions().remove(username);
		} else {
			existingMsg.getReactions().put(username, emoji);
		}
		
		return chatMessageService.save(existingMsg);
	}
	
	//入力中の表示
	@MessageMapping("/typing")
	@SendTo("/topic/messages")
	public ChatMessage handleTyping(ChatMessage message, Authentication authentication) {
		message.setSenderName(authentication.getName());
		message.setTyping(true); //入力中のフラグを立てる
		return message;
	}
	
	//メッセージ検索
	@GetMapping("/api/search")
	@ResponseBody //ページ遷移せずデータだけを返す
	public List<ChatMessage> search(@RequestParam String keyword){
		return chatMessageService.searchMessages(keyword);
	}
}
