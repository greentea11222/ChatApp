package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//ユーザー登録時のパスワード暗号化などのビジネスロジック
@Service
@RequiredArgsConstructor //private finalなフィールドのコンストラクタを自動生成する
public class UserService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String name) {
		return ???;
	}
}
