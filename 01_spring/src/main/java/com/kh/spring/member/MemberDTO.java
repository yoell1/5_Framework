package com.kh.spring.member;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor		// 기본 생성자
@AllArgsConstructor		// 모든 필드를 매개변수로 가지는 생성자
@Getter					// getter 메소드
@Setter					// setter 메소드
@ToString				// toString 메소드
@EqualsAndHashCode		// equals, hashCode 자동생성
public class MemberDTO {
/*
	ID	NUMBER   ---> int id
	NAME	VARCHAR2(50 BYTE)		--> String name
	EMAIL	VARCHAR2(100 BYTE)		--> String email
	AGE	NUMBER		--> int age
 */
	private int id;
	private String name;
	private String email;
	private int age;
}
