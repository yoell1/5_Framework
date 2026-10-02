package com.kh.community.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.community.board.model.dto.CommentDTO;

@Mapper
public interface CommentMapper {
	
	// 댓글 추가
	int insertComment(CommentDTO comment);
	
	// 댓글 단건 조회 - commentId
	CommentDTO selectCommentById(Long commentId);
	
	// 댓글 삭제
	int deleteComment(Long commentId);
	
	// 댓글 목록 조회 - boardId
	List<CommentDTO> selectCommentsByBoardId(Long boardId);
	
}





