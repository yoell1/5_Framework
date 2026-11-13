package com.kh.logdemo.stats.dto;

/** 상태코드별 요청 수 응답입니다. */
public record StatusCountResponse(int status, long count) {
}
