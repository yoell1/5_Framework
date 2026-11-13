package com.kh.logdemo.test.exception;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(int id) {
        super("상품을 찾을 수 없습니다: " + id);
    }
}
