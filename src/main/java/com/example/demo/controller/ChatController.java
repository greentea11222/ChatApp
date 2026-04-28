package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ChatMessageService;

@Controller
//チャット画面全体の表示
public class ChatController {
	
	private ChatMessageService chatMessageService = null;
	
	private UserRepository userRepository = null;
	
	private final SimpMessagingTemplate messagingTemplate;
	
	//コンストラクタ
	public ChatController(ChatMessageService chatMessageService, 
			UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
		this.chatMessageService = chatMessageService;
		this.userRepository = userRepository;
		this.messagingTemplate = messagingTemplate;
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
	
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event){
		//接続イベントを検知した際の処理
	}
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		
		if(username != null) {
			ChatMessage leaveMessage = new ChatMessage();
			leaveMessage.setType("LEAVE");
			leaveMessage.setSenderName(username);
			messagingTemplate.convertAndSend("/topic/messages", leaveMessage);
		}
	}
	
	private String extractUrl(String content) {
		//URLを抽出する正規表現
		String urlRegex = "https?:[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+";
		Pattern pattern = Pattern.compile(urlRegex);
		Matcher matcher = pattern.matcher(content);
		
		//最初に見つかったURLを返す
		if (matcher.find()) {
			return matcher.group();
		}
		
		return null;
	}
	
	//メッセージ送信時の処理の中で呼び出す
	private void fillLinkPreview(ChatMessage message) {
		String url = message.getContent();
		if (url != null) {
//			try {
				//タイムアウト設定を追加しておくと、サイトが重い時にフリーズしない
//				Document doc = Jsoup.connect(url).timeout(3000).get();
//				
//				//OPGタグを取得
//				String title = doc.select("meta[property=og:title]").attr("content");
//				if(title.isEmpty()) title = doc.title();
//				
//				String image = doc.select("meta[property=og:image]").attr("content");
//				String description = doc.select("meta[property=og:description]").attr("content");
	//			message.setLinkTitle(title);
	//			message.setLinkImage(image);
	//			message.setLinkDesciption(description);
			
//			}catch(IOException e) {
//				System.err.println("URLプレビューの取得に失敗しました: " + e.getMessage());
//			}
		}
	}
}
