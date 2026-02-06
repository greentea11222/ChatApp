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
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/register", "/h2-console/**").permitAll() //会員登録とDB確認画面は全員
				.anyRequest().authenticated() //それ以外のチャット画面などはログイン必須
			)
			.formLogin(login -> login
				.loginPage("/login") //自作のログインページを使う
				.defaultSuccessUrl("/chat", true) //ログイン成功時の移動先
			)
			.logout(logout -> logout
				.logoutSuccessUrl("/login") //ログアウトしたらログイン画面へ
				.permitAll()
			);
		
		//H2コンソールを表示するための特別設定
		http.csrf(csrf -> csrf.disable());
		http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
		
		return http.build();
	}
	
}
