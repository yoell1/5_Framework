package com.example.iris.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 모든 컨트롤러에 공통으로 적용되는 예외 처리. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ModelServerException.class)
    public ResponseEntity<String> handleModelServerError(ModelServerException e) {
        return ResponseEntity.status(e.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getBody());
    }
}
