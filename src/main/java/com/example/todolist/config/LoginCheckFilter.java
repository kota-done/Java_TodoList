package com.example.todolist.config;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component //アプリ起動時にフィルターも作動する
//Filterインターフェースの実装によりdoFilter init destroyの3つの抽象メソッドをもつ。
public class LoginCheckFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//HttpはServletのそれぞれサブクラス
		HttpServletRequest req = (HttpServletRequest) request; //URLのパスはrequestに保持されているが、取得にはHttpにダウンキャストする必要がある。
		HttpServletResponse res = (HttpServletResponse) response;

		String uri = req.getRequestURI();
		if (uri.startsWith("/todo") || uri.startsWith("/task")) { //TodoもしくはTaskを表示しようしているかどうかURLから確認。
			//sessionが存在するかの確認＝ログインの動作を実行したかどうか
			HttpSession session = req.getSession(false);
			if (session == null) {
				//ない場合、ログイン画面にリダイレクト
				res.sendRedirect("/login");
			} else {
				//セッションはあるが、アカウントIDは正しくあるか？＝ログイン完了しているか
				Integer accountId = (Integer)session.getAttribute("accountId");
				if (accountId == null) {
					//同じくログイン画面にリダイレクト
					res.sendRedirect("/login");
				} else {
					//ログイン完了している場合
					chain.doFilter(request, response); //
				}
			}
		} else {
			//フィルターチェック対象外、コントローラーへリクエストを渡す
			chain.doFilter(request, response); //
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
