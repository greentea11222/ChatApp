package com.example.demo.model;

import java.util.Collection;
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

//ユーザー情報を保持
@Getter
@Setter
@Entity //データベースのテーブルとして扱う宣言
public class User implements UserDetails{
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
	
	/*
	 * 以下、UserDetailsインターフェースのオーバーライド
	 * userDetailsのメソッドのうち3つはdefaultがついているためオーバーライド不要
	 * getPassword()はUserクラスのgetterで実装済み
	 * getUsername()については、ユーザー名の変数名が違うためオーバーライドが必要
	 */
	
	//ユーザーが持っている権限を返す
	//Enumのroleを文字列に変換し、それを権限として判定
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return List.of(new SimpleGrantedAuthority(role.name()));
	}
	
	@Override
	public String getUsername() {
		return this.name;
	}
	
	
}
