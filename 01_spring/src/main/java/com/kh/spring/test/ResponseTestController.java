package com.kh.spring.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/test")
public class ResponseTestController {
	// ============= 응답 방식 ====================
	
	/*
	 * @ResponseBody : 화면(View) 없이 결과를 바로 텍스트로 응답
	 */
	@ResponseBody
	@GetMapping		// => /test
	public String responseBodyTest() {
		return "결과를 텍스트로 응답!";
	}
	
	// 기본적으로 forward 방식으로 응답 처리됨!
	@GetMapping("/index")		// => /test/index
	public String responseIndex() {
		
		// prefix, suffix
		// {prefix}리턴값{suffix}
		// => /WEB-INF/views/페이지경로.jsp 이 파일을 ViewResolver 가 매칭시켜 응답해줄 것임!
		// return "포워드처리할페이지경로";
		return "test/index";  // => /WEB-INF/views/test/index.jsp
	}
	
	// TODO: 요청 받을 주소 => [GET] /test/model-forward
	// @GetMapping("/test/model-forward")	// => /test/test/model-forward
	@GetMapping("/model-forward")		// => /test/model-forward
	public String modelForwardTest(
			Model model
			) {
		// forward 방식으로 JSP(view)에 값을 전달할 때
		//		request 영역을 사용 => 요청이 끝나는 시점까지만 데이터를 저장해서 사용 가능
		
		// 기존(Servlet): HttpServletRequest 를 사용
		// Spring : Model 객체로 처리 가능!
		model.addAttribute("message", "Model을 통해 데이터 저장함!!!");
		
		return "test/modelTest";
		// => /WEB-INF/views/test/modelTest.jsp
	}
	
	// TODO: 요청 받을 주소 => [GET] /test/session/set
	@GetMapping("/session/set")
	public String sessionSetTest(
			HttpSession session
			) {
		// 세션 영역에 데이터를 저장 (HttpSession)
		//		=> 로그인 정보와 같은 여러 페이지에서 지속적으로 유지해야하는 데이터를 저장.
		session.setAttribute("user", "임수진");
		
		// * redirect 처리
		//	 "redirect:" 접두사 사용!
		return "redirect:/test/session";
		// 브라우저로 /test/session 재요청 지시
		// --> 새로운 요청이 발생함. 브라우저의 주소창에서 요청한 주소도 변경됨!
	}
	
	@GetMapping("/session")
	public String sessionTest(
			HttpSession session,
			Model model
			) {
		// 세션 영역에서 "user" 데이터를 추출 --> HttpSession
		String user = (String)session.getAttribute("user");
		
		// request 영역에 "message" 이름으로 "user" 데이터 저장 --> Model
		model.addAttribute("message", user);
		
		// modelTest.jsp 페이지로 포워드 처리
		return "test/modelTest";
	}
}







