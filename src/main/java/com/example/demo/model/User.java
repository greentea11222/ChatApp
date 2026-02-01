package com.example.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

//ユーザー情報を保持
@Getter
@Setter
@Entity //データベースのテーブルとして扱う宣言
public class User {
	@Id //主キーに設定
	@GeneratedValue(strategy = GenerationType.IDENTITY) //IDの自動採番
	private Long id;
	
	//ユーザー名は重複不可かつ空不可
	@Column(unique = true, nullable = false)
	private String name;
	
	//パスワードは空不可
	@Column(nullable = false)
	private String password;
	
	//ロール（権限）：USERかADMIN
	@Enumerated(EnumType.STRING)
	private Role role;
	
	//1対多のリレーション
	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatMessage> chats;
}
