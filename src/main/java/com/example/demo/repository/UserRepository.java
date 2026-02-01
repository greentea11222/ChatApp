package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

/*【メモ】インターフェースの条件
 * ①全てのメソッドが抽象メソッド
 * ②フィールドを持たない
 */

//DBへのユーザーデータの保存・検索
//JpaRepositoryを継承することで、save(), findAll(), deleteById()が使える
//JpaRepository<User, Long>：第1引数（User)をエンティティとして扱い、第2引数（Long）が主キー（＠Id）の型
public interface UserRepository extends JpaRepository<User, Long> {
	
	//ユーザー名からユーザーを検索
	//public abstractが省略されている（書かなくてOK）
	/*
	 * 【メソッドの流れ】
	 * ①メソッド名を解析し、SQLを勝手に生成・実行
	 * SELECT * FROM user WHERE name = ?;
	 * ②DBから返ってきたデータをJPAが解析
	 * ③Userインスタンスを生成し
	 * ④DBのname列の値でuser.setName()、password列の値でuser.setPassword()……と詰めていく
	 * （＝マッピング）
	 * ⑤見つかった場合はインスタンスをOptional.of(user)、
	 * 見つからなかった場合はOptional.empty()として返す
	 */
	Optional<User> findByName(String name);

}
