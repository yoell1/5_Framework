package com.kh.easymail.model;

import lombok.Data;

@Data
public class MailRequest {
	private String email;
	private String code;
}