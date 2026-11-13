package com.kh.logdemo.stats.dto;

/** 시간 구간별 전체 요청 수와 에러(5xx) 수 응답입니다. time은 "HH:mm" 형식입니다. */
public record TimelinePointResponse(String time, long total, long errors) {
}
