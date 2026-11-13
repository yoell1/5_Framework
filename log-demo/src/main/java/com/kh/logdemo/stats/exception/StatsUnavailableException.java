package com.kh.logdemo.stats.exception;

/** Elasticsearch에서 통계를 조회하지 못했을 때 발생합니다. */
public class StatsUnavailableException extends RuntimeException {

    public StatsUnavailableException(Throwable cause) {
        super("통계를 조회할 수 없습니다. Elasticsearch 실행 상태와 인덱스 템플릿 등록 여부를 확인하세요.", cause);
    }
}
