package com.kh.spring.member;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller	// @Component + 컨트롤러 계층의 기능이 추가된 어노테이션
			// -> 이 클래스의 메소드가 반환하는 문자열은 "View"의 이름으로 해석됨 (포워드)
@RequestMapping("/member")	// 클래스 레벨의 공통 URL 지정
							// -> 내부 메소드들은 매핑 URL 앞에 "/member"가 붙음
public class MemberController {
	
	// MemberService 클래스를 주입 (생성자 주입 방식)
	private final MemberService service;
	// @Autowired
	public MemberController(MemberService service) {
		this.service = service;
	}

	/**
	 * 회원 목록 조회 --> R (Read)
	 * URL : [GET] /member/list
	 */
	@GetMapping("/list")
	public String memberList(
			Model model
			) {
		List<MemberDTO> list = service.getMemberList();
		
		// 조회된 결과(list)를 request 영역에 저장 (k: memberList)
		model.addAttribute("memberList", list);
		
		// 포워드 처리됨! 
		return "member/list"; // => /WEB-INF/views/member/list.jsp
	}
	
	/**
	 * 회원 등록 --> C (Create)
	 * URL : [POST] /member/insert
	 * Parameter : age (나이), email (이메일), name (이름) => MemberDTO로 한번에 받을 수 있음
	 */
	@PostMapping("/insert")
	public String insert(@ModelAttribute MemberDTO member) {
		service.insertMember(member);
		
		return "redirect:/member/list"; 
	}
	
	/**
	 * 회원 삭제 --> D (Delete)
	 * URL : [GET] /member/delete/{id}
	 */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		service.deleteMember(id);
		// 회원 목록 페이지 재요청.... (리다이렉트)
		return "redirect:/member/list";
	}
	
	/**
	 * 회원 수정 --> U (Update)
	 * URL : [POST] /member/update
	 * 요청 파라미터 : {id: 회원번호, name: 이름, email: 이메일, age: 나이} --> MemberDTO
	 */
	@PostMapping("/update")
	public String update(@ModelAttribute MemberDTO member) {
		
		// 서비스로 수정 요청
		service.updateMember(member);
		return "redirect:/member/list";
	}
	
	/**
	 * 회원 수정 페이지 응답
	 * [GET] /member/update/회원번호
	 */
	@GetMapping("/update/{id}")
	public String updateForm(@PathVariable int id
							, Model model) {
		// 회원 번호를 기준으로 회원 정보를 조회
		MemberDTO member = service.getMember(id);
		
		// request 영역에 회원 정보 저장
		model.addAttribute("member", member);
		
		return "member/updateForm";
		// => prefix + 리턴값 + suffix
		//	  /WEB-INF/views/member/updateForm.jsp
	}
}









