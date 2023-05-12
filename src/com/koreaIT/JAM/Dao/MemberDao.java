package com.koreaIT.JAM.Dao;

import java.sql.Connection;

import com.koreaIT.JAM.Util.DBUtil;
import com.koreaIT.JAM.Util.SecSql;

public class MemberDao {
	
	private Connection conn;

	public MemberDao(Connection conn) {
		
		this.conn = conn;
		
	}

	public boolean isLoginDup(String loginId) {
		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*) > 0");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", loginId);

		return DBUtil.selectRowBooleanValue(conn, sql);
		
	}

	public void doJoin(String loginId, String loginPw, String name) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO member");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", `loginPw` = ?", loginPw);
		sql.append(", `name` = ?", name);

		DBUtil.insert(conn, sql);
		
	}

}
