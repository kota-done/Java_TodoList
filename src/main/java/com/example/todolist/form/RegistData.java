package com.example.todolist.form;

import org.hibernate.validator.constraints.Length;

import com.example.todolist.entity.Account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistData {
	//入力必須
	@NotBlank
	private String name;

	//入力必須＋長さ＝8〜16文字、半角英数字
	@NotBlank
	@Length(min = 8, max = 16)
	@Pattern(regexp = "^[a-zA-Z0-9]+$") //^[a-zA-Z0-9]+$が半角英数字の指定。それを正規表現に指定している。
	private String loginId;

	@NotBlank
	@Length(min = 8, max = 16)
	@Pattern(regexp = "^[a-zA-Z0-9]+$") //
	private String password1;

	@NotBlank
	@Length(min = 8, max = 16)
	@Pattern(regexp = "^[a-zA-Z0-9]+$") //
	private String password2;

	public Account toEntity() {
		return new Account(null, this.loginId, this.name, this.password1);
	}

}
