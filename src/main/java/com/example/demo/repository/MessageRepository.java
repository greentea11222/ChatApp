package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ChatMessage;

//過去のチャット履歴の保存・取得
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
	
	//チャットを全件取得し、投稿時間の古い順に並べ替え
	List<ChatMessage> findAllByOrderByTimestampAsc();
}
