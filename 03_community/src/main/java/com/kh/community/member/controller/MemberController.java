package com.kh.community.member.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.community.common.SessionConst;
import com.kh.community.common.dto.ApiResponse;
import com.kh.community.member.model.dto.MemberDTO;
import com.kh.community.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/*
 * "회원" 관련 화면 이동, 폼 처리 등을 담당할 컨트롤러
 */
@Controller
@RequestMapping("/member")
public class MemberController {
	// MemberService 를 DI 처리 (생성자 주입방식)
	private final MemberService service;
	public MemberController(MemberService service) {
		this.service = service;
	}
	
	
	
	// --- 화면 이동 요청 ----
	@GetMapping("/join")
	public String joinForm() {
		return "member/join";
	}
	
	@GetMapping("/login")
	public String loginForm() {
		return "member/login";
	}
	
	@GetMapping("/mypage")
	public String mypage() {
		return "member/mypage";
	}
	
	// --------------------
	
	@PostMapping("/join")
	public String join(@ModelAttribute MemberDTO member,
					   @RequestParam(required=false) MultipartFile profileImage,
					   RedirectAttributes redirectAttr) {
		System.out.println(member);
		System.out.println(profileImage);
		
		try {
			
			service.join(member, profileImage);
			
		} catch (IOException e) {
			e.printStackTrace();
			// "회원 가입 실패" 메시지를 저장 ---> 클라이언트에서 사용
			// * 세션 영역에 저장 (HttpSession)
			// * 리다이렉트 후 딱 한번 다음 요청에서만 사용되는 데이터 => RedirectAttributes
			redirectAttr.addFlashAttribute("error", "회원 가입 실패");
			
			// 예외 발생 시 회원 가입 페이지로 리다이렉트
			return "redirect:/member/join";
		}
		
		// 회원 가입 성공 시 로그인 페이지로 리다이렉트
		redirectAttr.addFlashAttribute("joinSuccess", true);
		return "redirect:/member/login";
	}
	
	// @ResponseBody : 응답 본문에 데이터를 담아 처리
	/*
	 * URL : [GET] /member/checkId?memberId=XXX
	 */
	@GetMapping("/checkId")
	@ResponseBody
	public ApiResponse<Boolean> checkId(String memberId) {
		
		boolean isDuplicate = service.isMemberIdCheck(memberId);
		
		String message = isDuplicate ? "이미 사용중인 아이디입니다." : "사용 가능한 아이디입니다.";
		
		return ApiResponse.success(message, isDuplicate);
	}

	@PostMapping("/login")
	public String login(String memberId, String memberPwd
						, @RequestParam(required=false) String redirectURL
						, HttpSession session
						, RedirectAttributes redirectAttr) {
		try {
			MemberDTO member = service.login(memberId, memberPwd);			
			// 로그인 성공 --> 세션에 로그인 정보 저장
			session.setAttribute(SessionConst.LOGIN_MEMBER, member);
		} catch (IllegalStateException e) {
			redirectAttr.addFlashAttribute("error", e.getMessage());
			return "redirect:/member/login";
		}
		
		if (redirectURL != null && !redirectURL.isBlank()) {
			return "redirect:" + redirectURL;
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate(); 	// 세션 자체를 만료(모두 삭제)
		}
		
		return "redirect:/";
	}
	
	@PostMapping("/withdraw")
	public String withdraw(HttpSession session) {
		// 세션에 저장되어 있는 사용자 정보를 추출
		MemberDTO loginMember = (MemberDTO)session.getAttribute(SessionConst.LOGIN_MEMBER);
		
		// 서비스에게 비즈니스 로직 요청
		service.withdraw(loginMember.getMemberId());
		
		// 세션 영역에서 모든 데이터 삭제(세션 만료)
		session.invalidate();
		
		// 메인 페이지로 리다이렉트
		return "redirect:/";
	}
}





