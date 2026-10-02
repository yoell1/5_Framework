package com.kh.community.board.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.community.board.model.dto.BoardDTO;
import com.kh.community.board.model.dto.BoardImageDTO;
import com.kh.community.board.model.dto.BoardListResult;
import com.kh.community.board.model.dto.BoardSearchCondition;
import com.kh.community.board.model.mapper.BoardMapper;
import com.kh.community.common.dto.PageInfo;
import com.kh.community.common.util.FileUploadUtil;
import com.kh.community.common.util.SavedFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper mapper;
	
	private final FileUploadUtil fileUploadUtil;
	
	@Value("${file.upload-dir.board}")
	private String boardUploadDir;

	@Override
	public BoardListResult getBoardList(BoardSearchCondition condition) {
		// 전체 게시글 수 조회
		int totalCount = mapper.selectBoardListCount(condition);
		
		// 페이징 정보 저장 (PageInfo 객체 생성)
		PageInfo pi = new PageInfo(condition.getPage(), condition.getSize(), totalCount);
		
		condition.setOffset(pi.getOffset());
		
		return new BoardListResult(mapper.selectBoardList(condition), pi);
		
	}

	@Override
	public Long writeBoard(BoardDTO board, List<MultipartFile> images) throws IllegalStateException, IOException {
		// DB에 게시글 정보 저장
		mapper.insertBoard(board);
		// => 매퍼가 실행된 후 BoardDTO에는 boardId 값이 채워짐
		
		Long boardId = board.getBoardId();
		// 이미지 파일 서버에 저장
		saveImages(boardId, images);
		
		// 게시글 id 값 리턴
		return boardId;
	}
	
	private void saveImages(Long boardId, List<MultipartFile> images) throws IllegalStateException, IOException {
		// 이미지가 없으면 메소드 종료
		if (images == null || images.isEmpty()) {
			return;
		}
		int order = 0;
		for (MultipartFile file : images) {
			// 서버에 이미지 파일 저장
			SavedFile saved = fileUploadUtil.save(file, boardUploadDir, "/uploads/board");
			if (saved == null) {
				continue;
			}
			
			// 저장된 이미지 정보를 기준으로 DTO 객체 생성
			BoardImageDTO boardImage = new BoardImageDTO(
										null,
										boardId,
										saved.getOriginalName(),
										saved.getSaveName(),
										saved.getPath(),
										order++,
										null
										);
			
			// DB에 게시글 이미지 저장
			mapper.insertBoardImage(boardImage);
		}
		
		
	}

	@Override
	public BoardDTO getBoardDetail(Long boardId) {
		// 상세페이지 접근 시 조회 수 1 증가 (업데이트)
		mapper.increaseViewCount(boardId);		
		
		// boardId에 해당하는 게시글 조회
		BoardDTO board = mapper.selectBoardDetail(boardId);
		
		// boardId에 해당하는 게시글 이미지 조회
		board.setImages( mapper.selectImagesByBoardId(boardId) );
		
		return board;
	}

	@Override
	public void deleteBoard(Long boardId) {
		// 삭제 전 이미지 정보 조회
		List<BoardImageDTO> images = mapper.selectImagesByBoardId(boardId);
		
		// 게시글 삭제
		mapper.deleteBoard(boardId);
		
		// 회원탈퇴처럼.. 이미지 서버에서 삭제
		if (images != null && !images.isEmpty()) {
			for (BoardImageDTO image : images) {
				fileUploadUtil.delete(image.getImagePath(), boardUploadDir);
			}
		}
	}
	
	

}





