package com.example.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.Category;
import com.example.todolist.entity.CategoryKey;

@Repository
public interface CategoryRepository extends JpaRepository<Category,CategoryKey> { 
	//localeで検索 -> codeの昇順（小->大）
	public List<Category> findByPkey_localeOrderByPkey_code(String locale); //[SELECT *FROM category WHERE locale='ja' ORDER BY code]に該当するメソッド宣言。
	//code,localeで検索　多くても1件なのでOrderBy不要
	public Category findByPkey(CategoryKey categoryKey);
}
