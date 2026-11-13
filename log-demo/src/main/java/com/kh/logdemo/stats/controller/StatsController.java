package com.kh.logdemo.stats.controller;

import com.kh.logdemo.stats.dto.PathStatResponse;
import com.kh.logdemo.stats.dto.StatsSummaryResponse;
import com.kh.logdemo.stats.dto.StatusCountResponse;
import com.kh.logdemo.stats.dto.TimelinePointResponse;
import com.kh.logdemo.stats.service.StatsService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그 분석 화면(index.html)이 호출하는 통계 API입니다.
 * minutes는 최근 몇 분을 집계할지 정합니다. (기본 30)
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<StatsSummaryResponse> summary(@RequestParam(defaultValue = "30") int minutes) {
        return ResponseEntity.ok(statsService.summary(minutes));
    }

    @GetMapping("/status")
    public ResponseEntity<List<StatusCountResponse>> status(@RequestParam(defaultValue = "30") int minutes) {
        return ResponseEntity.ok(statsService.statusCounts(minutes));
    }

    @GetMapping("/paths")
    public ResponseEntity<List<PathStatResponse>> paths(@RequestParam(defaultValue = "30") int minutes) {
        return ResponseEntity.ok(statsService.pathStats(minutes));
    }

    @GetMapping("/timeline")
    public ResponseEntity<List<TimelinePointResponse>> timeline(@RequestParam(defaultValue = "30") int minutes) {
        return ResponseEntity.ok(statsService.timeline(minutes));
    }
}
