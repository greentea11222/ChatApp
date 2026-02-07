package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//ログイン・会員登録画面の制御
@Controller
public class LoginController {
	@GetMapping("/login")
	public String login() {
		return ???;
	}
}
