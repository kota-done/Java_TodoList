package com.example.todolist.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.todolist.common.OpMsg;
import com.example.todolist.entity.Account;
import com.example.todolist.form.LoginData;
import com.example.todolist.form.RegistData;
import com.example.todolist.repository.AccountRepository;
import com.example.todolist.service.LoginService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final AccountRepository accountRepository;
	private final LoginService loginService;
	private final MessageSource messageSource;
	private final HttpSession session;

	//ログイン画面表示
	@GetMapping("/")
	public ModelAndView showLogin(ModelAndView mv) {
		mv.setViewName("loginForm");
		mv.addObject("loginData", new LoginData());
		return mv;
	}

	@GetMapping("/login")
	public ModelAndView login(ModelAndView mv) {
		mv.setViewName("loginForm");
		mv.addObject("loginData", new LoginData());
		return mv;
	}

	//ログインボタン押した時
	@PostMapping("/login/do")
	public String login(@ModelAttribute @Validated LoginData loginData, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, Locale locale) {
		//バリデーションチェック
		if (result.hasErrors()) {
			// エラーメッセージ(入力間違い)のセットと戻す
			String msg = messageSource.getMessage("msg.e.input_something_wrong", null, locale);
			model.addAttribute("msg", new OpMsg("E", msg));
			return "loginForm";
		}
		//サービスクラスでのチェック
		if (!loginService.isValid(loginData, result, locale)) {
			//エラーあり メッセージ
			String msg = messageSource.getMessage("msg.e.input_something_wrong", null, locale);
			model.addAttribute("msg", new OpMsg("E", msg));
			return "loginForm";
		}

		//セッション情報を一度クリア＝再ログインなどで残っていた場合エラーとなるため
		session.invalidate();
		//ログインしたユーザーのaccountIdをセッションへ格納する
		Account account = accountRepository.findByLoginId(loginData.getLoginId()).get(); //

		session.setAttribute("accountId", account.getId());
		//ログイン成功時メッセージ+タスク一覧画面に移動
		String msg = messageSource.getMessage("msg.i.login_successful",
				new Object[] { account.getLoginId(), account.getName() }, locale);
		redirectAttributes.addFlashAttribute("msg", new OpMsg("I", msg));
		return "redirect:/todo";

	}

	//ログアウト処理
	@GetMapping("/logout")
	public String logout(RedirectAttributes redirectAttributes, Locale locale) {
		//ログインしているセッション情報をクリア
		session.invalidate();
		//完了メッセージ＋リダイレクトでログイン画面に戻る
		String msg = messageSource.getMessage("msg.i.login_successful", null, locale);
		redirectAttributes.addFlashAttribute("msg", new OpMsg("I", msg));
		return "redirect:/";
	}

	//ユーザー新規登録・画面表示
	@GetMapping("/regist")
	public ModelAndView showRegist(ModelAndView mv) {
		mv.setViewName("registForm");
		mv.addObject("registData", new RegistData());
		return mv;
	}

	//ユーザー新規登録・登録実行
	@PostMapping("/regist/do")
	public String registNewUser(@ModelAttribute @Validated RegistData registData, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, Locale locale) {
		//エラーチェック
		boolean isValid = loginService.isValid(registData, result, locale);
		if (!result.hasErrors() && isValid) {
			//エラーがない場合＝登録
			Account account = registData.toEntity();
			accountRepository.saveAndFlush(account);
			//完了メッセージ＋リダイレクト
			String msg = messageSource.getMessage("msg.i.regist_successful",
					new Object[] { account.getName(), account.getLoginId() }, locale);
			redirectAttributes.addFlashAttribute("msg", new OpMsg("I", msg));
			return "redirect:/";
		} else {
			//エラーあり、エラーメッセージ＋戻る
			String msg = messageSource.getMessage("msg.e.input_something_wrong", null, locale);
			model.addAttribute("msg", new OpMsg("E", msg));
			return "registForm"; //入力内容をMode保存して再度表示。
		}
	}

	//ユーザー新規登録・キャンセル
	@GetMapping("/regist/cancel")
	public String registCancel() {
		return "redirect:/";
	}
}
