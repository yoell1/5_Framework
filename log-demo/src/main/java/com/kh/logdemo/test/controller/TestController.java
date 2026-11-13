package com.kh.logdemo.test.controller;

import com.kh.logdemo.test.dto.ItemResponse;
import com.kh.logdemo.test.dto.MessageResponse;
import com.kh.logdemo.test.dto.ResultResponse;
import com.kh.logdemo.test.dto.SlowResponse;
import com.kh.logdemo.test.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 분석할 로그를 만들어 내기 위한 테스트용 API입니다.
 * 요청을 받아 Service에 전달하고 결과를 DTO로 감싸 ResponseEntity로 응답하는 역할만 합니다.
 * 실제 처리와 로그 출력은 TestService가 담당합니다.
 */
@RestController
@RequestMapping("/api")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    /** 항상 빠르게 성공 */
    @GetMapping("/hello")
    public ResponseEntity<MessageResponse> hello() {
        return ResponseEntity.ok(new MessageResponse(testService.hello()));
    }

    /** 200ms ~ 3000ms 사이로 느리게 응답 */
    @GetMapping("/slow")
    public ResponseEntity<SlowResponse> slow() throws InterruptedException {
        return ResponseEntity.ok(new SlowResponse(testService.slow()));
    }

    /** 항상 예외 발생 (500) */
    @GetMapping("/error")
    public ResponseEntity<Void> error() {
        testService.fail();
        return ResponseEntity.ok().build();
    }

    /** 상품 조회 흉내: 숫자가 아니면 400, 0 이하이면 404 */
    @GetMapping("/items/{id}")
    public ResponseEntity<ItemResponse> item(@PathVariable String id) {
        return ResponseEntity.ok(ItemResponse.from(testService.findItem(id)));
    }

    /** 약 70% 정상, 15% 느림, 15% 에러. 트래픽 생성 스크립트가 주로 호출합니다. */
    @GetMapping("/random")
    public ResponseEntity<ResultResponse> random() throws InterruptedException {
        return ResponseEntity.ok(new ResultResponse(testService.random()));
    }
}
