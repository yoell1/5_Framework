package com.kh.logdemo.stats.dto;

/** 요약 통계 응답입니다. errorRate는 퍼센트(%), avgMs는 평균 응답시간(ms)입니다. */
public record StatsSummaryResponse(long total, long errors, double errorRate, double avgMs) {
}
