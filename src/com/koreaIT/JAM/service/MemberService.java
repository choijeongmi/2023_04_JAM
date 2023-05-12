package com.koreaIT.JAM.service;

import java.sql.Connection;

import com.koreaIT.JAM.Dao.MemberDao;

public class MemberService {

	MemberDao memberDao;

	public MemberService(Connection conn) {
		this.memberDao = new MemberDao(conn);
	}

	public boolean isLoginIdDup(String loginId) {

		return memberDao.isLoginDup(loginId);
	}

	public void dojoin(String loginId, String loginPw, String name) {
		memberDao.dojoin(loginId, loginPw, name);

	}

}
