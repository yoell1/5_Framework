package com.kh.easymail.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.easymail.model.MailRequest;
import com.kh.easymail.service.MailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {

	private final MailService service;

	/**
	 * 인증코드 발송
	 *
	 * @param email: 받는사람
	 * @return "ok"
	 * @throws Exception
	 */
	@PostMapping("/mail")
	public String sendMail(@RequestBody MailRequest request) throws Exception {
		String email = request.getEmail();

		if (email == null) {
			throw new Exception("필수 항목이 없습니다. (email)");
		}

		log.info("* email: {}", email);

		// 서비스를 통해 메일 전송
		service.sendCode(email);

		return "ok";
	}

	/**
	 * 인증코드 검증
	 *
	 * @param email: 받는 사람, code: 인증코드
	 * @return 인증 성공이면 "success", 실패면 "fail"
	 * @throws Exception
	 */
	@PostMapping("/mail/verify")
	public String verifyMail(@RequestBody MailRequest request) throws Exception {
		String email = request.getEmail();
		String code = request.getCode();

		if (email == null || code == null) {
			throw new Exception("필수 항목이 없습니다. (email, code)");
		}

		boolean result = service.verifyCode(email, code);
		log.info("* 인증 결과: {} → {}", email, result);

		return result ? "success" : "fail";
	}

}