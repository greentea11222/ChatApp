package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
	
	//送信者
	//多対1のリレーション。多くの投稿が1人のユーザーに紐づく。
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User sender;
	
}
