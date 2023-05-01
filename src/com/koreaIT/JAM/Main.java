package com.koreaIT.JAM;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		System.out.println("=== 프로그램 시작 ===");
		
		Scanner sc = new Scanner(System.in);

		List<Article> articles = new ArrayList<>();
		
		int lastArticleId = 0;
		
		while(true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine().trim();
			
			if(cmd.equals("exit")) {
				System.out.println("=== 프로그램 끝 ===");
				break;
			}
			
			
			if(cmd.equals("article write")) {
				System.out.println("== 게시물 작성 ==");
				
				int id = lastArticleId + 1;
				lastArticleId = id;
				
				System.out.println("제목 : \n");
				String title = sc.nextLine();
				System.out.println("내용 : \n");
				String body = sc.nextLine();
				
				Article article = new Article(id, title, body);
				articles.add(article);
				
				System.out.printf("%d번 게시물이 작성되었습니다.\n", id);
				
				
			}
			
			else if(cmd.equals("article list")) {
				System.out.println("== 게시물 리스트==");
				
				if(articles.size() == 0) {
					System.out.println("존재하는 게시물이 없습니다.");
					continue;
				}
				
				for(Article article : articles) {
					System.out.printf("%d    |%s    |%s    \n", article.id , article.title, article.body);
					
				}
				
				
			}
			
		}
		sc.close();


	}

}
