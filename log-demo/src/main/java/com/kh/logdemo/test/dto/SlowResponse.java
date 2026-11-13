package com.kh.logdemo.test.dto;

/** slow API의 응답 데이터입니다. 실제로 지연시킨 시간(ms)을 담습니다. */
public record SlowResponse(long delayMs) {
}
