package com.kh.mybatis.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.mybatis.model.dto.MemberDTO;
import com.kh.mybatis.model.mapper.MemberMapper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor	// final 키워드가 붙은 필드를 매개변수로 가지는 생성자
public class MemberController {
	
	// MemberMapper 주입 (생성자 주입 방식)
	private final MemberMapper mapper;
	/*
	 * 직접 생성자를 정의하는 방법 (롬복을 사용하지 않을 때)
	public MemberController(MemberMapper mapper) {
		this.mapper = mapper;
	}
	*/
	
	/**
	 * 회원 목록 조회
	 * URL: [GET] /member/list
	 * Param: x
	 * 응답: 회원목록페이지(WEB-INF/views/member/list.jsp) 포워딩
	 */
	@GetMapping("/list")
	public String memberList(Model model) {
		// DB에서 조회된 회원 목록을 request 영역에 저장 (k: memberList)
		List<MemberDTO> list = mapper.findAll();
		
		model.addAttribute("memberList", list);
		
		return "member/list";
	}
	
	/**
	 * 회원 추가
	 * URL: [POST] /member/insert
	 * Param: name(String), email(String), age(int)
	 * 응답: 회원목록페이지로 리다이렉트
	 */
	@PostMapping("/insert")
	public String memberInsert(
			// @RequestParam(value="name", defaultValue="xx") String name
			// String name, String email, int age
			@ModelAttribute MemberDTO member,
			HttpSession session
			) {
		int result = mapper.insert(member);
		
		// 추가 성공 시 "회원 가입 성공했습니다" 메시지를 저장
		//	   실패 시 "회원 가입 실패했습니다" 메시지를 저장 
		// ==> 리다이렉트 처리 시 재요청되므로 "session" 영역에 저장 (HttpSession)
		if (result > 0) {
			session.setAttribute("message", "회원 가입 성공했습니다!");
		} else {
			session.setAttribute("message", "회원 가입 실패했습니다..");
		}
		
		
		return "redirect:/member/list";
	}
	
	/**
	 * 회원 가입 페이지
	 * URL : [GET] /member/insert
	 * Param : x
	 * 응답 : 회원 가입 페이지(/WEB-INF/views/member/insertForm.jsp) 포워딩
	 */
	@GetMapping("/insert")
	public String memberInsertForm() {
		return "member/insertForm";
	}
}





