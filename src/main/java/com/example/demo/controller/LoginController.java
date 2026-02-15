package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	//会員登録画面から送られてきたデータを保存する
	@PostMapping("/register")
	//引数のModel（UI Model)はControllerからHTML（画面）へ一時的にデータを渡すための箱。
	public String register(@Valid User user, BindingResult result, Model model) {
		
		//アノテーションによる入力チェック
		if (result.hasErrors()) {
			//エラーがある場合は登録画面に戻る
			//エラーメッセージは自動的にBindingResultからHTMLへ引き継がれる
			return "register";
		}
		
		//サービスでユーザー情報をDBに保存
		try {
			userService.registerUser(user);
			//リダイレクト：URLにアクセスし直すため、Model内のデータは消える。
			//処理が正常に終了した場合に使う（データの二重送信を防ぐため）
			return "redirect:/login"; //登録が成功したらログイン画面に遷移
			
		//ユーザー名が重複していたらエラーメッセージ
		} catch(RuntimeException e) {
			//エラーメッセージをModelに詰めて、登録画面を表示し直す
			//addAttribute("名前", 値）：HTML側で使う名前とデータを紐付けて箱に入れる
			model.addAttribute("errorMessage", e.getMessage());
			//フォワード：Modelに入れたデータをHTMLに渡せるので、入力エラーが起きた時などに使う（エラー内容などを画面に表示するため）
			return "register";	//失敗時は登録画面を再表示
		}
	}
}
