package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

//ログイン・会員登録画面の制御
@Controller
@RequiredArgsConstructor //フィールドを引数として使うコンストラクタを自動で作ってくれる
public class LoginController {
	
	//クラスに@RequiredArgsConstructorアノテーションをつけるか、手動でコンストラクタを作らないと
	//「初期化されていない可能性がある」と言われてエラーになる
	private final UserService userService;
	
	//ログイン画面を表示
	@GetMapping("/login")
	public String showLoginPage() {
		//src/main/resources/templates/login.htmlを表示（templatesフォルダ以降のパスのみ書く。拡張子は不要）
		return "login";
	}
	
	//会員登録画面を表示
	@GetMapping("/register")
	public String showRegisterPage() {
		return "register";
	}
	
	//会員登録画面から送られてきたデータを保存する
	@PostMapping("/register")
	public String register(User user) {
		//サービスに処理を任せる
		userService.registerUser(user);
		
		return "redirect:/login"; //登録が終わったらログイン画面に遷移
	}
}
