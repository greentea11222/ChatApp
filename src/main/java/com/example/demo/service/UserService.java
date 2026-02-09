package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

//ユーザー登録時のパスワード暗号化などのビジネスロジック
@Service
@RequiredArgsConstructor //private finalなフィールドのコンストラクタを自動生成する
public class UserService implements UserDetailsService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	//会員登録のメソッド
		public void registerUser(User user) {
			//メモ：ここにユーザー名が重複しないかチェックを入れる
			
			//パスワードを暗号化
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			
			//権限を設定（一律でUSER権限に）
			user.setRole(Role.ROLE_USER);
			
			//DBに保存
			userRepository.save(user);
		}
	
	//ユーザー名をDBで探し、見つかったらUserインスタンスを返す
	@Override
	public UserDetails loadUserByUsername(String name) {
		return userRepository.findByName(name)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません" + name));
	}
}
