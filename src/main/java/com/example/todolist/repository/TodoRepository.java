package com.example.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.Todo;

//エンティティで操作するテーブルを指定し、リポジトリで具体的な操作を記述する。

//第一引数：対象のエンティティ　第二引数：＠Idが指定されているクラス（id=Integer）を継承することでCRUD処理が一通り抽象メソッドとして実装した。
@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {

	//アカウント毎へのテーブル連携9/27 PDF変更
	List<Todo> findByOwnerIdOrderById(Integer accountId);

}
