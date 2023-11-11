package com.example.todolist.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data //主キーの一意性を補償するため、Dataで自動生成
@NoArgsConstructor
@AllArgsConstructor
public class CategoryKey implements Serializable {
	private static final long serialVersionUID =1L; //シリアライズ化させるために必要な記述。オブジェクトをバイト直列にすることで、照会しやすくする。
	
	@Column(name="code")
	private String code;
	
	@Column(name = "locale")
	private String locale;
}
