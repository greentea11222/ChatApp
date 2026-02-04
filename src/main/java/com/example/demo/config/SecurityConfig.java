package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//ログイン設定やアクセス制限の設定
@Configuration		//設定ファイルであることを示す
@EnableWebSecurity	//Spring Security機能を有効にする
public class SecurityConfig {
	//パスワードを暗号化（ハッシュ化）する
	@Bean	//このメソッドが返すオブジェクトをSpringの管理下において使い回せるようにする
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
	}
	
}
