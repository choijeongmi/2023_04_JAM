package com.koreaIT.JAM.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.JAM.Util.Util;
import com.koreaIT.JAM.dto.Article;
import com.koreaIT.JAM.service.ArticleService;
import com.koreaIT.JAM.session.Session;

public class ArticleController {

	private Scanner sc;
	List<Article> articles = new ArrayList<>();
	ArticleService articleService;

	public ArticleController(Connection conn, Scanner sc) {

		this.sc = sc;
		this.articleService = new ArticleService(conn);
	}

	public void doWrite(Scanner sc) {

		if (!Session.isLogided()) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}
		System.out.println("== 게시물 작성 ==");

		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();

		int id = articleService.doWrite(title, body, Session.loginedMemberId);

		System.out.printf("%d번 게시글이 생성되었습니다.\n", id);

	}

	public void showList(String cmd) {
		
		String searchKeyword = cmd.substring("article list".length()).trim();

		List<Article> articles = articleService.getArticles(searchKeyword);

		if (articles.size() == 0) {
			System.out.println("존재하는 게시물이 없습니다.");
			return;
		}
		System.out.println("== 게시물 리스트==");
		if(searchKeyword.length() > 0) {
			System.out.println("검색어 : "+ searchKeyword);
			
		}
		System.out.println(" 번호   | 제목   | 작성자	|	날짜   |	조회수");

		for (Article article : articles) {
			System.out.printf("%d    |%s   	|%s		|%s    \n", article.id, article.title, article.writerName,
					Util.datetimeFormat(article.regDate), article.vCnt);

		}

		articleService.getArticles(searchKeyword);

	}

	public void showDetail(String cmd) {

		int id = Integer.parseInt(cmd.split(" ")[2]);

		int affectedRow = articleService.increaseVCnt(id);

		if (affectedRow == 0) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}
		Article article = articleService.getArticle(id);

		System.out.printf("== %d번 게시물 상세보기==\n", id);
		System.out.printf("번호 : %d\n", article.id);
		System.out.printf("제목 : %s\n", article.title);
		System.out.printf("내용 : %s\n", article.body);
		System.out.printf("작성자 : %s\n", article.writerName);
		System.out.printf("조회수 : %s\n", article.vCnt);
		System.out.printf("작성날짜 : %s\n", Util.datetimeFormat(article.regDate));
		System.out.printf("수정날짜 : %s\n", Util.datetimeFormat(article.updateDate));

	}

	public void doModify(String cmd) {
		if (!Session.isLogided()) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(id);

		if (article == null) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}

		if (article.memberId != Session.loginedMemberId) {
			System.out.println("게시글 수정 권한이 없습니다.");
			return;
		}

		System.out.printf("%d번 게시글을 수정하겠습니다.\n", id);
		System.out.printf("수정할 제목 : ");
		String title = sc.nextLine();
		System.out.printf("수정할 내용 : ");
		String body = sc.nextLine();

		articleService.doModify(title, body, id);

		System.out.printf("%d번 게시글이 수정되었습니다\n", id);

	}

	public void doDelete(String cmd) {
		if (!Session.isLogided()) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(id);

		if (article == null) {

			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;

		}

		if (article.memberId != Session.loginedMemberId) {
			System.out.println("게시글 수정 권한이 없습니다.");
			return;
		}
		articleService.doDelete(id);

		System.out.printf("%d번 게시글을 삭제하겠습니다.\n", id);

		System.out.printf("%d번 게시글이 삭제되었습니다\n", id);

	}

}
