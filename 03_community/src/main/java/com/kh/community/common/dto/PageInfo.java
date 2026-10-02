package com.kh.community.common.dto;

import lombok.Getter;

/*
 * 페이징 정보를 계산하고 저장하는 클래스
 * 
 * - 게시판 목록처럼 데이터가 많아질 경우 한 번에 전부 가져올 필요없이
 *   필요한 만큼만 끊어서 보여주고자 할 경우 사용하는 방법 (페이징 처리)
 *   
 * - 화면에 보여줄 개수(size)와 현재 페이지 번호, 전체 게시글 수를 통해 계산을 수행함
 */
@Getter
public class PageInfo {
	private int page;		// 현재 페이지 번호
	private int size;		// 한 페이지에 보여줄 게시글 개수
	private int totalCount;	// 전체 목록 개수 (게시글 개수)

	private int totalPages;	// 전체 페이지 수
	private int startPage;	// 화면에서 보여줄 페이지 시작 번호
	private int endPage;	// 화면에서 보여줄 페이지 끝 번호
	private boolean hasPrevGroup;	// 이전 페이지 그룹 존재 여부
	private boolean hasNextGroup;	// 다음 페이지 그룹 존재 여부
	
	private static final int PAGE_GROUP_SIZE = 5;
	// 하단에 한번에 보여줄 페이지 번호 개수 (고정)
	
	public PageInfo(int page, int size, int totalCount) {
		this.page = page < 1 ? 1 : page;
		this.size = size;
		this.totalCount = totalCount;
		
		// 전체 페이지 수 : Math.ceil(전체 게시글 수 / (double)한 페이지당 게시글 수)
		this.totalPages = (int)Math.ceil(totalCount / (double)size);
		
		// 표시되는 페이지 번호 : page = 7, page_group_size = 5 --> 6 ... 10
		this.startPage = ((this.page - 1) / PAGE_GROUP_SIZE) * PAGE_GROUP_SIZE + 1;
		this.endPage = Math.min(startPage + PAGE_GROUP_SIZE - 1, totalPages);
		
		// 이전/다음 그룹 존재 여부
		this.hasPrevGroup = startPage > 1;
		this.hasNextGroup = endPage < totalPages;
	}
	
	public int getOffset() {
		return (page - 1) * size;
	}
}






