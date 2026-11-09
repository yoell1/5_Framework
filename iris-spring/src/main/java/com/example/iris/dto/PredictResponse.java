package com.example.iris.dto;

import java.util.Map;

/** FastAPI 모델 서버 /predict 응답 본문. */
public record PredictResponse(
        String label,
        double probability,
        Map<String, Double> probabilities) {
}
