package com.example.iris.exception;

import org.springframework.http.HttpStatusCode;

/** 모델 서버 호출 실패. 클라이언트에 돌려줄 상태 코드와 JSON 본문을 담는다. */
public class ModelServerException extends RuntimeException {

    private final HttpStatusCode status;
    private final String body;

    public ModelServerException(HttpStatusCode status, String body, Throwable cause) {
        super(body, cause);
        this.status = status;
        this.body = body;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
