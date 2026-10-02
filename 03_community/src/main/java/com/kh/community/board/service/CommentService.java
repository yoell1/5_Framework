package com.kh.community.board.service;

import java.util.List;

import com.kh.community.board.model.dto.CommentDTO;

public interface CommentService {
	
	// 댓글 작성
	CommentDTO addComment(Long boardId, String content, String writerId);
	
	// 댓글 목록 조회
	List<CommentDTO> getComments(Long boardId);
	
	// 댓글 삭제
	void deleteComment(Long commentId, String requestMemberId);

}
