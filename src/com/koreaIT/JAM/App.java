package com.koreaIT.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
	public void run() {
		System.out.println("=== 프로그램 시작 ===");

		Scanner sc = new Scanner(System.in);

		Connection conn = null; // 접속 정보를 저장하기 위한 역할

		PreparedStatement pstmt = null; // 쿼리를 db에게 날려주는 역할

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();

				if (cmd.equals("exit")) {
					System.out.println("=== 프로그램 끝 ===");
					break;
				}

//				ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ작성ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

				if (cmd.equals("article write")) {
					System.out.println("== 게시물 작성 ==");

					System.out.printf("제목 : ");
					String title = sc.nextLine();
					System.out.printf("내용 : ");
					String body = sc.nextLine();

					try {

						String sql = "INSERT INTO article";
						sql += " SET regDate = NOW()";
						sql += ", updateDate = NOW()";
						sql += ", title = '" + title + "'";
						sql += ", `body` = '" + body + "'";

						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();

					} catch (SQLException e) {
						System.out.println("에러: " + e);
					}

					System.out.println("게시글이 생성되었습니다.");

				}

//				ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ리스트ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

				else if (cmd.equals("article list")) {
					System.out.println("== 게시물 리스트==");

					ResultSet rs = null; // 결과 셋팅 타입

					List<Article> articles = new ArrayList<>();

					try {

						String sql = "SELECT * ";
						sql += " FROM article";
						sql += " ORDER BY id DESC"; // id를 기준으로 내림차순정렬

						pstmt = conn.prepareStatement(sql);
						rs = pstmt.executeQuery(); // 결과를 받아야 힐 때

						// db쿼리에서 압축을 해 결과를 rs에 넣을때 반복문으로 압축해제를 해준 느낌.
						while (rs.next()) { // next() = 아래의 명령사항이 있을땐 true 값을 없을땐 false 로 반복문을 빠져나온다.
							int id = rs.getInt("id");
							String regDate = rs.getString("regDate");
							String updateDate = rs.getString("updateDate");
							String title = rs.getString("title");
							String body = rs.getString("body");

							Article article = new Article(id, regDate, updateDate, title, body);
							articles.add(article);
						}

					} catch (SQLException e) {
						System.out.println("에러: " + e);
					} finally {
						try {
							if (rs != null && !rs.isClosed()) {
								rs.close();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

					if (articles.size() == 0) {
						System.out.println("존재하는 게시물이 없습니다.");
						continue;
					}

					for (Article article : articles) {
						System.out.printf("%d    |%s    |%s    \n", article.id, article.title, article.body);

					}

				}
//				ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ수정ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

				else if (cmd.startsWith("article modify ")) {
					String[] cmdBits = cmd.split(" ");
					int id = Integer.parseInt(cmdBits[2]);

					System.out.printf("%d번 게시물을 수정하겠습니다.\n", id);

					System.out.printf("수정할 제목 : ");
					String title = sc.nextLine();
					System.out.printf("수정할 내용 : ");
					String body = sc.nextLine();

					try {

						String sql = "UPDATE article";
						sql += " SET title = '" + title + "'";
						sql += ", `body` = '" + body + "'";
						sql += ", updateDate = NOW()";
						sql += ", regDate = NOW()";
						sql += " WHERE id = " + id;

						pstmt = conn.prepareStatement(sql);
						pstmt.executeUpdate();

					} catch (SQLException e) {
						System.out.println("에러: " + e);
					}

					System.out.printf("%d번 게시물이 수정되었습니다", id);
				}
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
