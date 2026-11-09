package com.example.iris.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.iris.dto.PredictRequest;
import com.example.iris.dto.PredictResponse;
import com.example.iris.service.PredictService;

/** 브라우저 요청을 받아 PredictService로 위임한다. */
@RestController
@RequestMapping("/api")
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    @PostMapping("/predict")
    public PredictResponse predict(@RequestBody PredictRequest request) {
        return predictService.predict(request);
    }
}
