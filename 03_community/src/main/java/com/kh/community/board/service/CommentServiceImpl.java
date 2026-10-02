package com.kh.community.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.community.board.model.dto.CommentDTO;
import com.kh.community.board.model.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	
	private final CommentMapper mapper;

	@Override
	public CommentDTO addComment(Long boardId, String content, String writerId) {
		// 댓글 내용 검토
		if (content == null || content.isBlank()) {
			throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
		}
		
		CommentDTO comment = new CommentDTO();
		comment.setBoardId(boardId);
		comment.setContent(content);
		comment.setMemberId(writerId);
		
		mapper.insertComment(comment);	// 실행 후 commentId 가 채워짐
		
		// commentId 로 다시 조회한 후 결과를 반환
		return mapper.selectCommentById( comment.getCommentId() );
	}

	@Override
	public List<CommentDTO> getComments(Long boardId) {
		return mapper.selectCommentsByBoardId(boardId);
	}

	@Override
	public void deleteComment(Long commentId, String requestMemberId) {
		
		// 댓글id 기준으로 댓글 조회
		CommentDTO comment = mapper.selectCommentById(commentId);
		
		// 조회된 댓글이 없으면 예외 발생
		if (comment == null) {
			throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
		}
		
		// 작성자와 요청자가 다를 경우 예외 발생
		if (comment.getMemberId() == null || !comment.getMemberId().equals(requestMemberId)) {
			throw new SecurityException("본인이 작성한 댓글만 삭제할 수 있습니다.");
		}
		
		// 댓글 삭제
		mapper.deleteComment(commentId);
	}

	
	
}
