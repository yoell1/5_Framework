package com.kh.community.board.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.community.board.model.dto.BoardDTO;
import com.kh.community.board.model.dto.BoardListResult;
import com.kh.community.board.model.dto.BoardSearchCondition;

public interface BoardService {
	
	// 게시글 목록 조회
	BoardListResult getBoardList(BoardSearchCondition condition);

	// 게시글 추가
	Long writeBoard(BoardDTO board, List<MultipartFile> images) throws IllegalStateException, IOException;
	
	// 게시글 상세 조회
	BoardDTO getBoardDetail(Long boardId);
	
	// 게시글 삭제
	void deleteBoard(Long boardId);
	
}





