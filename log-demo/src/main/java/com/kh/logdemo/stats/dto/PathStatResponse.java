package com.kh.logdemo.stats.dto;

/** API 경로별 요청 수와 평균 응답시간(ms) 응답입니다. */
public record PathStatResponse(String path, long count, double avgMs) {
}
