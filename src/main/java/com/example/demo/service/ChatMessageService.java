package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.ChatMessage;
import com.example.demo.repository.ChatMessageRepository;

//メッセージの保存やフィルタリング処理
@Service
public class ChatMessageService {
	private final ChatMessageRepository repository;
	
	//コンストラクタ
	public ChatMessageService(ChatMessageRepository repository) {
		this.repository = repository;
	}
	
	//メッセージを保存する
	public ChatMessage save(ChatMessage message) {
		return repository.save(message);
	}
	
	//過去の全メッセージを取得
	public List<ChatMessage> getAllMessages() {
		return repository.findAllByOrderByTimestampAsc();
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
