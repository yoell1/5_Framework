package com.kh.logdemo.test.dto;

import com.kh.logdemo.test.model.Item;

/** 상품 조회 API의 응답 데이터입니다. Model(Item)을 응답용으로 변환합니다. */
public record ItemResponse(int id, String name) {

    public static ItemResponse from(Item item) {
        return new ItemResponse(item.id(), item.name());
    }
}
