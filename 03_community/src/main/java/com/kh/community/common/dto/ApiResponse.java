package com.kh.community.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * 비동기 요청 시 응답으로 전달할 공통 객체
 * 
 * 클라이언트에서 항상 정해진 포맷으로 응답이 온다는 가정을 할 수 있기 때문에,
 * 매번 응답에 따른 코드를 그때그때 만들지 않고 표준적으로 코드 구성이 가능해짐!
 * 
 * 제네릭을 사용하는 이유 : 응답데이터는 그때그때 타입이 다르기 때문
 */
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
	
	private boolean success;   // 성공 여부
	private String message;	   // 성공, 실패에 따른 메시지
	private T data;				// 응답 데이터

	// ---- 성공 응답 시 사용할 정적 메소드 ----
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, null, data);
	}
	
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, message, data);
	}	
	
	// ---- 실패 응답 시 사용할 정적 메소드 ----
	public static <T> ApiResponse<T> fail(String message) {
		return new ApiResponse<>(false, message, null);
	}
}





