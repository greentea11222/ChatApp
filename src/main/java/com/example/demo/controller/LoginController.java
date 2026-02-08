package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.User;

//ログイン・会員登録画面の制御
@Controller
public class LoginController {
	
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
		//パスワードを暗号化して保存
		
		
		return "redirect:/login"; //登録が終わったらログイン画面に遷移
	}
}
