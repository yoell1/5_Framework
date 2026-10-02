package com.kh.community.board.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardImageDTO {
	// TB_BOARD_IMAGE 테이블 기준으로 작성
	private Long imageId;
	private Long boardId;
	private String originalName;
	private String saveName;
	private String imagePath;
	private int imageOrder;
	private LocalDateTime createAt;
}

