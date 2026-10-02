package com.kh.easymail.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender sender;

	/** 제한 시간: 3분 */
	private static final Duration LIMIT = Duration.ofMinutes(3);

	/** 인증 정보 (인증코드 + 생성 시간) */
	private record AuthInfo(String code, LocalDateTime createdAt) {}

	/** 이메일별 인증 정보를 메모리에 저장 (key: 이메일, value: 인증 정보) */
	private final Map<String, AuthInfo> authStore = new ConcurrentHashMap<>();

	/**
	 * [1. 인증코드 발송]
	 * @param email 받는 사람
	 */
	public void sendCode(String email) throws MessagingException {
		// 1-1) 랜덤 인증코드 생성
		String code = createCode();

		// 1-2) 인증코드와 생성 시간을 이메일별로 메모리(Map)에 저장
		authStore.put(email, new AuthInfo(code, LocalDateTime.now()));

		// 1-3) 인증코드를 메일 본문에 담아 전송
		String subject = "[KH] 인증 코드";
		String html = "<h2>이메일 인증 코드</h2>"
					+ "<p>인증 코드는 <b style='font-size:20px;'>" + code + "</b> 입니다.</p>"
					+ "<p>" + LIMIT.toMinutes() + "분 안에 입력해 주세요.</p>";
		sendHTMLMail(subject, html, email);

		log.info("* 인증코드 발송: {} → {}", email, code);
	}

	/**
	 * [2. 코드 검증]
	 * @param email 받는 사람
	 * @param code  사용자가 입력한 인증코드
	 * @return 인증 성공 여부
	 */
	public boolean verifyCode(String email, String code) {
		AuthInfo info = authStore.get(email);

		// 발송 기록이 없으면 실패
		if (info == null) {
			return false;
		}

		// 2-2) 생성 시간이 제한 시간(3분)을 넘겼는지 확인 → 넘겼으면 메모리에서 삭제
		Duration elapsed = Duration.between(info.createdAt(), LocalDateTime.now());
		if (elapsed.compareTo(LIMIT) > 0) {
			authStore.remove(email);
			return false;
		}

		// 2-1) 전달된 인증코드와 저장된 인증코드가 일치하는지 확인
		if (!info.code().equals(code)) {
			return false;
		}

		// 인증 성공 → 같은 코드로 다시 인증하지 못하도록 삭제
		authStore.remove(email);
		return true;
	}

	/**
	 * 6자리 랜덤 인증코드 생성 (100000 ~ 999999)
	 */
	private String createCode() {
		Random random = new Random();
		int code = random.nextInt(900000) + 100000;
		return String.valueOf(code);
	}

	/**
	 * 일반 텍스트 메일 전송
	 */
	private void sendMail(String subject, String text, String to) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setSubject(subject);
		message.setText(text);
		message.setTo(to);

		sender.send(message);
	}

	/**
	 * HTML 메일 전송
	 */
	private void sendHTMLMail(String subject, String html, String to) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

		helper.setSubject(subject);
		helper.setText(html, true);
		helper.setTo(to);

		sender.send(message);
	}

}