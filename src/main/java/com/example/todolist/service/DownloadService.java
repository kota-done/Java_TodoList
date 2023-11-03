package com.example.todolist.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.todolist.common.Utils;
import com.example.todolist.entity.AttachedFile;
import com.example.todolist.repository.AttachedFileRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DownloadService {
	private final AttachedFileRepository attachedFileRepository;
	private final MessageSource messageSource;

	@Value("${attached.file.path}")
	private String ATTACHED_FILE_PATH;

	//添付ファイルdownload処理
	public void downloadAttachedFile(int afId, HttpServletResponse response) {
		//添付ファイルの情報を取得
		AttachedFile af = attachedFileRepository.findById(afId).get();
		//拡張子からContent-Typeを求める
		String fileName = af.getFileName();
		String fext = fileName.substring(fileName.lastIndexOf(".") + 1); //拡張子（.）の後ろ
		String contentType = Utils.ext2contentType(fext);
		//ダウンロードするファイル
		String downLoadFilePath = Utils.makeAttahcedFilePath(ATTACHED_FILE_PATH, af);
		File downloadFile = new File(downLoadFilePath); //

		//ダウンロードファイルの送信
		BufferedInputStream bis = null;
		OutputStream out = null;
		try {
			if (contentType.equals("")) {
				//バイナリで送信
				response.setContentType("application/force-download");
				//ローカル保存
				response.setHeader("Content-Disposition",
						"attachment;filename=\"" + URLEncoder.encode(af.getFileName(), "UTF-8") + "\""); //
			} else {
				//拡張子に対応するContent-Type
				response.setContentType(contentType);
				//別タブ表示
				response.setHeader("Content-Disposition", "inline"); //
			}
			//ファイルサイズ
			response.setContentLengthLong(downloadFile.length());
			//ファイルの内容をブラウザに出力
			bis = new BufferedInputStream(new FileInputStream(downLoadFilePath));
			out = response.getOutputStream();
			byte[] buff = new byte[8 * 1024];
			int nRead = 0;
			while ((nRead = bis.read(buff)) != -1) {
				out.write(buff, 0, nRead); //
			}
			out.flush();
			//ファイルclose
			bis.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//不正なダウンロードリクエストの場合の処理
	public void invalidDownloadRequest(HttpSession session, HttpServletResponse response, Locale locale) {
		// セッションの無効化
		session.invalidate();

		//メッセージ取得してセット。
		String mes1 = messageSource.getMessage("Something.Wrong1", null, locale); //
		String mes2 = messageSource.getMessage("Something.Wrong2", null, locale); //
		String mes3 = messageSource.getMessage("Something.Wrong3", null, locale); //

		//エラー画面を出力
		try {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("<meta charset='UTF-8>'");
			out.println("<title>Todo List</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>" + mes1 + "</h1>");
			out.println("<h2>" + mes2 + "</h2>");
			out.println("<a href='/login>'" + mes3 + "</a>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
