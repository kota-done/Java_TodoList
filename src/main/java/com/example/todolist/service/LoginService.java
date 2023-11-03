package com.example.todolist.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.todolist.entity.Account;
import com.example.todolist.form.LoginData;
import com.example.todolist.form.RegistData;
import com.example.todolist.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginService {

	private final MessageSource messageSource;
	private final AccountRepository accountRepository;

	//ログインチェック
	public boolean isValid(LoginData loginData, BindingResult result, Locale locale) {
		//ログインIDが登録されているか。
		Optional<Account> account = accountRepository.findByLoginId(loginData.getLoginId());
		if (account.isEmpty()) {
			//登録が確認できない
			FieldError fieldError = new FieldError(result.getObjectName(), "password",
					messageSource.getMessage("NotFound.loginData.loginId", null, locale));
			result.addError(fieldError);
			return false;
		}
		//パスワードがあっているか
		if (!account.get().getPassword().equals(loginData.getPassword())) {
			//パスワード間違い
			FieldError fieldError = new FieldError(result.getObjectName(), "password",
					messageSource.getMessage("NotFound.loginData.password", null, locale));
			result.addError(fieldError);
			return false;
		}
		return true;
	}
	//登録画面用のチェック
	public boolean isValid(RegistData registData, BindingResult result, Locale locale) {
		if (!registData.getPassword1().equals(registData.getPassword2())) {
			//パスワード不一致の場合
			FieldError fieldError = new FieldError(result.getObjectName(), "password2",
					messageSource.getMessage("Unmatch.registData.password", null, locale));
			result.addError(fieldError);
			return false;
		}
		//ログインIDがすでに使われているか
		Optional<Account> account = accountRepository.findByLoginId(registData.getLoginId());
		if (account.isPresent()) {
			//登録されている場合
			FieldError fieldError = new FieldError(result.getObjectName(), "loginId",
					messageSource.getMessage("AlreadyUsed.registData.loginId", null, locale));
			result.addError(fieldError);
			return false;
		}
		return true;
	}
}
