package com.kh.community.board.model.dto;

import lombok.Getter;
import lombok.Setter;

// 게시판 검색 조건을 저장하기 위한 클래스
@Getter
@Setter
public class BoardSearchCondition {
	// 검색 파라미터 : 카테고리, 검색 종류, 키워드
	private String category;	// 전체, 자유, 공지, 질문
	private String searchType;	// titleContent, title, content, writer
	private String keyword;		// 검색어. 입력 값이 없으면 null
	
	// 페이징 관련
	private int size = 10;		// 한 페이지에 보여줄 게시글 개수 (고정)
	private int page = 1;		// 페이지 번호 (기본값 1)
	
	// 쿼리문 실행 시 사용할 값
	private int offset;		// 건너뛸 행수
}







