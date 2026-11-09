package com.example.iris.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** FastAPI 모델 서버 /predict 요청 본문 (꽃받침/꽃잎 길이·너비, cm). */
public record PredictRequest(
        @JsonProperty("sepal_length") double sepalLength,
        @JsonProperty("sepal_width") double sepalWidth,
        @JsonProperty("petal_length") double petalLength,
        @JsonProperty("petal_width") double petalWidth) {
}
