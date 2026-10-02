package com.kh.community.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.community.board.model.dto.CommentDTO;
import com.kh.community.board.model.dto.CommentRequest;
import com.kh.community.board.service.CommentService;
import com.kh.community.common.SessionConst;
import com.kh.community.common.dto.ApiResponse;
import com.kh.community.member.model.dto.MemberDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/*
 * "댓글" 관련 요청을 담당할 컨트롤러
 */
@RestController  // @Controller + @ResponseBody
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentApiController {
	
	private final CommentService service;

	@PostMapping("/board/{boardId}/comment")
	public ResponseEntity<ApiResponse<CommentDTO>> addComment(@PathVariable Long boardId,
															  @RequestBody CommentRequest commentRequest,
															  HttpSession session) {
		// 로그인한 사용자 정보 추출
		MemberDTO loginMember = (MemberDTO)session.getAttribute(SessionConst.LOGIN_MEMBER);
		
		// 게시글 번호, 사용자 아이디, 댓글 정보를 저장
		try {
			
			CommentDTO comment = service.addComment(boardId, commentRequest.getContent(), loginMember.getMemberId());
			return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(comment));
		
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
		}
	}
	
	// 댓글 삭제
	//  - 요청방식: delete
	//  - 요청주소: /api/comments/댓글번호
	//  - 요청파라미터(데이터): x
	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<ApiResponse<Long>> deleteComment(@PathVariable Long commentId,
														   HttpSession session) {
		MemberDTO loginMember = (MemberDTO) session.getAttribute(SessionConst.LOGIN_MEMBER);
		
		try {
			service.deleteComment(commentId, loginMember.getMemberId());
			return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("성공적으로 삭제하였습니다", commentId));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.fail(e.getMessage()));
		} catch (SecurityException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail(e.getMessage()));
		}
	}
}










