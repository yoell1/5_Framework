package com.kh.logdemo.common.exception;

import com.kh.logdemo.common.dto.ErrorResponse;
import com.kh.logdemo.stats.exception.StatsUnavailableException;
import com.kh.logdemo.test.exception.ItemNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 예외를 한 곳에서 처리하면서 로그를 남깁니다.
 * log.error("메시지", 예외) 형태로 호출하면 스택트레이스가 JSON의 stack_trace 필드에 들어갑니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(ItemNotFoundException e) {
        log.warn("404 처리: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    /** 없는 주소나 favicon.ico 같은 정적 파일 요청입니다. 500이 아니라 404로 응답합니다. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> noResource(NoResourceFoundException e) {
        log.warn("404 처리: 존재하지 않는 경로 {}", e.getResourcePath());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> badRequest(IllegalArgumentException e) {
        log.warn("400 처리: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(StatsUnavailableException.class)
    public ResponseEntity<ErrorResponse> statsUnavailable(StatsUnavailableException e) {
        log.warn("503 처리: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> serverError(Exception e) {
        log.error("처리되지 않은 예외 발생", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("internal server error"));
    }
}
