package com.koreaIT.JAM.Dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.koreaIT.JAM.Util.DBUtil;
import com.koreaIT.JAM.Util.SecSql;

public class ArticleDao {

	private Connection conn;

	public ArticleDao(Connection conn) {

		this.conn = conn;

	}

	public int doWrite(String title, String body, int loginedMemberId) {

		SecSql sql = new SecSql();
		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", memberId = ?", loginedMemberId);
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);

		return DBUtil.insert(conn, sql);

	}

	public List<Map<String, Object>> getArticles(String searchKeyword) {
		SecSql sql = new SecSql();

		sql.append("SELECT A.*, M.name AS writerName");
		sql.append("FROM article AS A");
		sql.append("INNER JOIN `member` AS M");
		sql.append("ON A.memberId = M.id");
		if(searchKeyword.length() > 0) {
		sql.append("WHERE title LIKE CONCAT('%', ? , '%')",searchKeyword);
		}
		sql.append("ORDER BY id DESC");

		return DBUtil.selectRows(conn, sql);

	}

	public Map<String, Object> getArticle(int id) {
		SecSql sql = new SecSql();
		sql.append("SELECT A.*, M.name AS writerName");
		sql.append("FROM article AS A");
		sql.append("INNER JOIN `member` AS M");
		sql.append("ON A.memberId = M.id");
		sql.append("WHERE A.id = ?", id);

		return DBUtil.selectRow(conn, sql);

	}

	public void doModify(String title, String body, int id) {
		SecSql sql = SecSql.from("UPDATE article");
		sql.append("SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);
		sql.append(" WHERE id = ?", id);

		DBUtil.update(conn, sql);

	}

	public int getArticleCount(int id) {
		SecSql sql = SecSql.from("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public void doDelete(int id) {
		SecSql sql = new SecSql();
		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);

		DBUtil.delete(conn, sql);

	}

	public int increaseVCnt(int id) {
		SecSql sql = SecSql.from("UPDATE article");
		sql.append("SET vCnt = vCnt + 1"); 
		sql.append(" WHERE id = ?", id);
		
		return DBUtil.update(conn, sql);
		
	}

 
}
