package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;

import lombok.Getter;
import lombok.Setter;

//メッセージ内容を保持
@Getter
@Setter
@Entity
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//投稿内容
	@Column(columnDefinition = "TEXT") //DB上の型をTEXTに指定
	private String content;
	
	//送信日時
	private LocalDateTime timestamp;
	
	//送受信用の名前（画面表示用）
	private String senderName;
	
	//送信者
	//多対1のリレーション。多くの投稿が1人のユーザーに紐づく。
	@ManyToOne
	@JoinColumn(name = "user_id")
	//送受信時にはUser全体を無視（JSONデータに変換しない）
	@com.fasterxml.jackson.annotation.JsonIgnore
	private User sender;
	
	//画像データを保持するフィールド（Base64方式）
	@Column(columnDefinition = "TEXT")
	private String imageData;
	
	//送信者のアイコン画像
	@Column(columnDefinition = "TEXT")
	private String senderIcon;
	
	//削除フラグ
	private boolean deleteFlg;
	
	//既読をつけたユーザーのSet
	@ElementCollection
	private Set<String> readByUsers = new HashSet<>();
	
	//リアクション
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "message_reactions")
	@MapKeyColumn(name = "user_name")
	@Column(name = "emoji")
	private Map<String, String> reactions = new HashMap<>();
	
	//リアクションをまとめるメソッド
	public Map<String, Integer> getReactionCounts(){
		Map<String, Integer> counts = new HashMap<>();
		for (String emoji: reactions.values()) {
			counts.put(emoji, counts.getOrDefault(emoji, 0) + 1);
		}
		return counts;
	}
	
	//既読数を返す
	public int getReadCount() {
		return readByUsers.size();
	}
	
	//コンストラクタ
	public ChatMessage() {}
	public ChatMessage(String content, String senderName, User sender) {
		this.content = content;
		this.senderName = senderName;
		this.sender = sender;
		this.timestamp = LocalDateTime.now();
	}
	
}
