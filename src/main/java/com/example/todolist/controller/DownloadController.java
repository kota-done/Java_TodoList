package com.example.todolist.controller;

import java.util.Locale;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.entity.AttachedFile;
import com.example.todolist.entity.Todo;
import com.example.todolist.repository.AttachedFileRepository;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.DownloadService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DownloadController {

	public final DownloadService downloadService;
	//ユーザー認証に伴い追加9/28
	private final TodoRepository todoRepository;
	private final AttachedFileRepository attachedFileRepository;
	private final HttpSession session;

	//添付ファイルのダウンロード処理
	@GetMapping("/todo/af/download/{afId}")
	public void downloadAttachedFile(@PathVariable(name = "afId") int afId, HttpServletResponse response,
			Locale locale) {
		//添付ファイル情報を取得
		Optional<AttachedFile> someAf = attachedFileRepository.findById(afId);
		someAf.ifPresentOrElse(af -> {
			//添付ファイルが存在するので、添付先のTodo情報を取得
			Optional<Todo> someTodo = todoRepository.findById(af.getTodoId());
			someTodo.ifPresentOrElse(todo -> {
				//Todoが存在する
				//アクセス者のセッションからaccountIdを取得
				Integer accountId = (Integer) session.getAttribute("accountId");
				//操作者のTodoかどうか確認
				if (todo.getOwnerId().equals(accountId)) {
					//本人確認ができたのでファイルダウンロード
					downloadService.downloadAttachedFile(afId, response);
				} else {
					//本人でないので戻す
					downloadService.invalidDownloadRequest(session, response, locale);
				}
			}, () -> {
				//Todoが存在しない
				downloadService.invalidDownloadRequest(session, response, locale);
			});
		}, () -> {
			//添付ファイルが存在しない
			downloadService.invalidDownloadRequest(session, response, locale);
		});
	}
}
