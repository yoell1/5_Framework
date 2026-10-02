package com.kh.community.board.model.dto;

import java.util.List;

import com.kh.community.common.dto.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardListResult {
	private List<BoardDTO> boardList; // 조회 목록
	private PageInfo pageInfo; 		  // 페이징 정보
}
