package com.koreaIT.JAM.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.koreaIT.JAM.Dao.ArticleDao;
import com.koreaIT.JAM.dto.Article;

public class ArticleService {

	private ArticleDao articleDao;

	public ArticleService(Connection conn) {
		this.articleDao = new ArticleDao(conn);
	}

	public int doWrite(String title, String body, int loginedMemberId) {

		return articleDao.doWrite(title, body, loginedMemberId);

	}

	public List<Article> getArticles(String searchKeyword) {
		List<Map<String, Object>> articleListMap = articleDao.getArticles(searchKeyword);

		List<Article> articles = new ArrayList<>();

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public Article getArticle(int id) {

		Map<String, Object> articleMap = articleDao.getArticle(id);
		if (articleMap.isEmpty()) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return null;
		}

		return new Article(articleMap);
	}

	public void doModify(String title, String body, int id) {

		articleDao.doModify(title, body, id);
	}

	public int getArticleCount(int id) {

		return articleDao.getArticleCount(id);
	}

	public void doDelete(int id) {
		articleDao.doDelete(id);
	}

	public int increaseVCnt(int id) {
		
		return articleDao.increaseVCnt(id);

	}

//	public int getwriterId(int id) {
//		 
//		return articleDao.getwriterId(id);
//	}

}
