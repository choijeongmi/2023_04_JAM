package com.koreaIT.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.koreaIT.JAM.Util.DBUtil;
import com.koreaIT.JAM.Util.SecSql;
import com.koreaIT.JAM.controller.ArticleController;
import com.koreaIT.JAM.controller.MemberController;

public class App {
	public void run() {
		System.out.println("=== 프로그램 시작 ===");

		Scanner sc = new Scanner(System.in);

		Connection conn = null; // 접속 정보를 저장하기 위한 역할

		

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");
			
			MemberController memberController = new MemberController(conn,sc);
			ArticleController articleController = new ArticleController(conn,sc);
			
			

			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();

				if (cmd.equals("exit")) {
					System.out.println("=== 프로그램 끝 ===");
					break;
				}
//				ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ회원가입ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
				
				if (cmd.equals("member join")) {
					memberController.doJoin();
				}
				

//				ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ작성ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

				else if (cmd.equals("article write")) {
					articleController.doWrite(sc);

				}

//				ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ리스트ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

				else if (cmd.equals("article list")) {

					articleController.showList();

				}
//				ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ상세보기ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
				else if (cmd.startsWith("article detail ")) {
					String[] cmdBits = cmd.split(" ");
					int id = Integer.parseInt(cmdBits[2]);

					SecSql sql = new SecSql();
					sql.append("SELECT *");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);

					Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

					if (articleMap.isEmpty()) {
						System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
						continue;
					}

					Article article = new Article(articleMap);

					System.out.printf("== %d번 게시물 상세보기==\n", id);

					System.out.printf("번호 : %d\n", article.id);
					System.out.printf("제목 : %s\n", article.title);
					System.out.printf("내용 : %s\n", article.body);
					System.out.printf("작성날짜 : %s\n", article.regDate);
					System.out.printf("수정날짜 : %s\n", article.updateDate);

				}


//				ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ수정ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

				else if (cmd.startsWith("article modify ")) {
					String[] cmdBits = cmd.split(" ");
					int id = Integer.parseInt(cmdBits[2]);


					SecSql sql = SecSql.from("SELECT COUNT(*)");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);

					int articleCount = DBUtil.selectRowIntValue(conn, sql);

					if (articleCount == 0) {
						System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
						continue;
					}

					System.out.printf("%d번 게시글을 수정하겠습니다.\n", id);
					System.out.printf("수정할 제목 : ");
					String title = sc.nextLine();
					System.out.printf("수정할 내용 : ");
					String body = sc.nextLine();

					sql = SecSql.from("UPDATE article");
					sql.append("SET updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);
					sql.append(" WHERE id = ?", id);

					DBUtil.update(conn, sql);

					System.out.printf("%d번 게시글이 수정되었습니다\n", id);
				}
//					ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ삭제ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					else if (cmd.startsWith("article delete ")) {
						String[] cmdBits = cmd.split(" ");
						int id = Integer.parseInt(cmdBits[2]);

						SecSql sql = new SecSql();
						sql.append("SELECT COUNT(*) > 0");
						sql.append("FROM article");
						sql.append("WHERE id = ?", id);

						boolean isHaveArticle = DBUtil.selectRowBooleanValue(conn, sql);

						if (!isHaveArticle) {

							System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
							continue;

						}
						sql = new SecSql();
						sql.append("DELETE FROM article");
						sql.append("WHERE id = ?", id);

						DBUtil.delete(conn, sql);
						
						System.out.printf("%d번 게시글을 삭제하겠습니다.\n", id);

						System.out.printf("%d번 게시글이 삭제되었습니다\n", id);
					}else {
						System.out.println("존재하지 않는 명령어입니다.");
					}
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {

			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sc.close();
	}
}
