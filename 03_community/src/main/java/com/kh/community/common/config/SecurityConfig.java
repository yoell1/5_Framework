package com.kh.community.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * * Spring Security 설정 클래스
 * 
 * - Spring Security : 스프링 기반 어플리케이션의 인증(Authentication)과 인가(Authorization)를 담당하는
 * 					   보안 프레임워크.
 * 
 *   => pom.xml 파일에 spring-boot-starter-security 의존성 추가
 *   => 별도 설정 없이도 모든 요청에 로그인을 요구하는 기본 보안이 적용됨
 *   
 * --> 현재 프로젝트에서는 스프링 시큐리티의 기본 로그인/인가 기능을 사용하지 않고
 *     HttpSession 을 직접 관리하는 방식으로 로그인을 구현할 것임!
 *     => 이 클래스를 통해 시큐리티의 기본 기능을 전부 비활성화하고, 로그인 검사 여부는 별도의 인터셉터를 통해 처리할 것임!
 *     
 *     비밀번호 암호화 시 사용하기 위해서 시큐리티를 제거하지는 않을 것임!
 */
@Configuration	// 이 클래스가 스프링의 설정 클래스임을 선언. 내부에 @Bean 메소드가 있으면 스프링 컨테이너가 메소드가 반환해주는 객체를 빈으로 등록함.
@EnableWebSecurity   // 스프링 시큐리티 웹 보안 기능을 활성화하는 어노테이션. SecurityFilterChain 빈을 정상적으로 동작하게 함!
public class SecurityConfig {
	
	/*
	 * * SecurityFilterChain : HTTP 요청에 적용할 보안 규칙을 정의하는 객체(빈)
	 * 
	 * 스프링 시큐리티는 요청이 들어오면 여러 필터를 순서대로 거치게 함.
	 * SecurityFilterChain은 그 필터들의 묶음이며, 여기서 각 필터의 동작 방식을 설정함.
	 */
	@Bean	// 해당 메소드가 반환하는 객체(SecurityFilterChain)를 스프링 빈으로 등록
	public SecurityFilterChain filterChain(HttpSecurity http) {
		/*
		 * HttpSecurity : HTTP 요청에 대한 보안 설정을 체이닝 방식으로 구성하는 빌더 객체.
		 * 
		 * - CSRF (Cross-Site Request Forgery) : 사이트 간 요청 위조
		 * 		=> 세션 기반 인증 + fetch(REST API) 사용할 예정으로 비활성화
		 * - formLogin : 시큐리티 기본 로그인 폼
		 * - HTTP Basic 인증 : 인증 헤더에 ID/PW를 Base64로 인코딩해서 보내는 인증 방식
		 * - logout : 시큐리티 기본 로그아웃 처리
		 * -----> 비활성화 처리 (AbstractHttpConfigurer::disable)
		 * 						=> 각 보안 기능의 설정 클래스에 정의된 disable 메소드를 참조 
		 * 
		 * - authorizeHttpRequests : 인증 여부에 따른 접근 제어
		 */
		
		http.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // 모든 요청 허용
		
		return http.build();
	}

	
	/**
	 * * BCryptPasswordEncoder : 비밀번호 단방향 해시 암호화
	 * 
	 *   - 단방향 암호화
	 *     : 암호화는 가능하지만 복호화는 불가능한 방식.
	 *       DB에 비밀번호 평문을 저장하는 대신, 암호화된 해시값을 저장함.
	 *       --> DB가 유출되어도 원본 비밀번호는 알 수 없음!
	 *       
	 *   - BCrypt 특징
	 *     : 같은 비밀번호라도 암호화할 때마다 다른 해시값이 적용됨. (salt 적용)
	 *       일치 여부를 확인할 때, passwordEncoder.matches(평문, 암호문) 으로 검증
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}










