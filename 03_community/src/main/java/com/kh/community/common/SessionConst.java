package com.kh.community.common;

/*
 * 세션에 값을 저장할 때 사용하는 키를 한 곳에 모아서 관리하기 위한 클래스
 * -> 키값들을 상수로 관리
 */
public class SessionConst {
	
	public static final String LOGIN_MEMBER = "loginMember";
	
	// 이 클래스는 정적 상수만 관리하기 위해서 객체 생성을 막아두기.
	private SessionConst() {}

}
