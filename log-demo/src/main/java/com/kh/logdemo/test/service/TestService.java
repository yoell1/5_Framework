package com.kh.logdemo.test.service;

import com.kh.logdemo.test.exception.ItemNotFoundException;
import com.kh.logdemo.test.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 테스트 API의 실제 처리와 로그 출력을 담당합니다.
 * 정상, 느린 응답, 에러 상황을 일부러 만들어 분석할 로그를 쌓습니다.
 */
@Service
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    public String hello() {
        log.info("hello API 호출");
        return "hello";
    }

    /** 200ms ~ 3000ms 사이로 지연시키고, 지연 시간을 돌려줍니다. */
    public long slow() throws InterruptedException {
        long delay = 200 + (long) (Math.random() * 2800);
        log.info("slow API 처리 시작, 지연 {}ms 예정", delay);
        Thread.sleep(delay);
        return delay;
    }

    /** 항상 예외를 던집니다. */
    public void fail() {
        log.warn("error API가 곧 예외를 던집니다");
        throw new IllegalStateException("의도적으로 발생시킨 예외");
    }

    /** id가 숫자가 아니면 IllegalArgumentException, 0 이하이면 ItemNotFoundException을 던집니다. */
    public Item findItem(String id) {
        int itemId;
        try {
            itemId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("id는 숫자여야 합니다: " + id, e);
        }
        if (itemId <= 0) {
            throw new ItemNotFoundException(itemId);
        }
        return new Item(itemId, "item-" + itemId);
    }

    /** 15%는 에러, 15%는 느린 응답, 나머지는 정상 응답입니다. */
    public String random() throws InterruptedException {
        int dice = (int) (Math.random() * 100);
        if (dice < 15) {
            throw new IllegalStateException("랜덤 에러 (dice=" + dice + ")");
        }
        if (dice < 30) {
            Thread.sleep(500 + (long) (Math.random() * 2000));
        }
        return "ok";
    }
}
