package com.koreaIT.JAM.dto;
//dto,vo > 데이터베이스에서 가져온 데이터 덩어리~

import java.time.LocalDateTime;
import java.util.Map;

public class Article {
	public int id;
	public LocalDateTime regDate;
	public LocalDateTime updateDate;
	public int memberId;
	public String title;
	public String body;
	public int vCnt;
	
	public String writerName;
	
 



	public Article(Map<String, Object> articleMap) {
		this.id = (int) articleMap.get("id");
		this.vCnt = (int) articleMap.get("vCnt");
		this.regDate = (LocalDateTime) articleMap.get("regDate");
		this.updateDate = (LocalDateTime) articleMap.get("updateDate");
		this.memberId = (int) articleMap.get("memberId");
		this.title = (String) articleMap.get("title");
		this.body = (String) articleMap.get("body");
		this.writerName = (String) articleMap.get("writerName");
	}



	@Override
	public String toString() {
		return "Article [id=" + id + ", regDate=" + regDate + ", updateDate=" + updateDate + ",  title=" + title
				+ ", body=" + body + ", vCnt="+ vCnt + ",]";
	}

//	, memberId="+ memberId + ",
	

}
