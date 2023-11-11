package com.example.todolist.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "category")
@Data
@ToString(exclude = "todoList")
@NoArgsConstructor
public class Category implements Serializable{
	private static final long serialVersionUID = 1L; //Category.Keyを埋め込むために、こちらにも実装。
	
	@EmbeddedId //複合キーであることを宣言。
	private CategoryKey pkey; //
	
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(mappedBy = "category")
	@OrderBy("id asc")
	private List<Todo> todoList = new ArrayList<>();
	
	//埋め込み元である、CategoryKeyのカラムを引数にして、pkeyに代入。Categoryでは、pkey+nameを受け取るようにして、セットする。
	public Category(String code,String locale,String name) {
		this(code,locale);
		this.name=name;
	}
	public Category(String code, String locale) {
		this.pkey=new CategoryKey(code,locale);
		this.name="";
	}

}
