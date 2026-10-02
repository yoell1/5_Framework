package com.kh.mybatis.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.mybatis.model.dto.MemberDTO;

/*
	01_spring 프로젝트에서 MemberDAO를 대체하는 MyBatis의 Mapper 인터페이스!
	
	@Mapper : 이 인터페이스는 MyBatis의 Mapper로써 스프링이 어플리케이션 시작 시
			  인터페이스의 구현체를 자동으로 만들어서 Bean으로 등록함.
			  (개발자가 직접 SQL 실행 코드를 작성하지 않아도 됨!)
*/
@Mapper
public interface MemberMapper {
	
	// 전체 회원 목록 조회 (데이터 조회/SELECT)
	List<MemberDTO> findAll();
	
	// 회원 등록 (데이터 추가/INSERT)
	int insert(MemberDTO member);
	
}





