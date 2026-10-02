package com.kh.community.common.interceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kh.community.common.SessionConst;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*
 * HandlerInterceptor
 * : 서블릿과 컨트롤러 사이에서 공통 로직을 끼워넣을 수 있는 스프링 MVC 확장 지점
 * 
 * 로그인이 필요한 기능마다 매번 체크하는 로직을 반복하지 않고
 * 이 인터셉터를 통해 로그인이 필요한 경로와 아닌 경로를 분리해서 로그인 여부를 검사함
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// preHandle : 컨트롤러로 진입하는 시점에 동작
		
		HttpSession session = request.getSession(false);
		
		boolean isLoggedIn = session != null && session.getAttribute(SessionConst.LOGIN_MEMBER) != null;
		
		if (isLoggedIn) {
			return true;		// 로그인 되어 있는 경우 그대로 진행 (컨트롤러)
		}
		
		// 로그인되어 있지 않은 경우 로그인 페이지로 리다이렉트
		// response.sendRedirect("/member/login");
		
		// 기존에 요청했던 주소로 로그인 후 리다이렉트 처리
		String redirectURL = URLEncoder.encode(request.getRequestURI(), StandardCharsets.UTF_8);
		response.sendRedirect("/member/login?redirectURL=" + redirectURL);
		return false;
		
	}
	
}







