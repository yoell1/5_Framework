package com.kh.logdemo.stats.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.logdemo.stats.dto.PathStatResponse;
import com.kh.logdemo.stats.dto.StatsSummaryResponse;
import com.kh.logdemo.stats.dto.StatusCountResponse;
import com.kh.logdemo.stats.dto.TimelinePointResponse;
import com.kh.logdemo.stats.exception.StatsUnavailableException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

/**
 * Elasticsearch에 쌓인 접근 로그를 집계해서 통계를 만듭니다.
 *
 * 집계 대상은 /api/ 로 시작하는 접근 로그입니다. 통계 화면이 호출하는 /api/stats 요청은 제외합니다.
 * http_status, http_path, duration_ms 같은 필드를 집계하려면 인덱스 템플릿이 먼저 등록되어 있어야 합니다.
 */
@Service
public class StatsService {

    private static final Logger log = LoggerFactory.getLogger(StatsService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final RestClient restClient;

    public StatsService(@Value("${elasticsearch.url}") String elasticsearchUrl) {
        // Elasticsearch 전용 라이브러리(Spring Data Elasticsearch 등)가 있지만,
        // ES 7.10.1과 Spring Boot 3의 버전 호환성 문제로 사용하지 않고 RestClient로 직접 호출합니다.

        // JDK HttpClient의 기본값은 HTTP/2 시도(h2c 업그레이드)라서 서버에 따라 충돌할 수 있습니다.
        // HTTP/1.1로 고정해 Elasticsearch와 안정적으로 통신합니다.
        HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        this.restClient = RestClient.builder()
                .baseUrl(elasticsearchUrl)
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .build();
    }

    /** 전체 요청 수, 에러(5xx) 수, 에러율, 평균 응답시간 */
    public StatsSummaryResponse summary(int minutes) {
        JsonNode res = search("""
                {"size":0,"track_total_hits":true,"query":%s,
                 "aggs":{
                   "errors":{"filter":{"range":{"http_status":{"gte":500}}}},
                   "avg_ms":{"avg":{"field":"duration_ms"}}}}
                """.formatted(baseQuery(minutes)));

        long total = res.path("hits").path("total").path("value").asLong();
        long errors = res.path("aggregations").path("errors").path("doc_count").asLong();
        double avgMs = res.path("aggregations").path("avg_ms").path("value").asDouble();
        double errorRate = total == 0 ? 0 : errors * 100.0 / total;
        return new StatsSummaryResponse(total, errors, round1(errorRate), round1(avgMs));
    }

    /** 상태코드별 요청 수 (요청이 많은 순서로 5개) */
    public List<StatusCountResponse> statusCounts(int minutes) {
        JsonNode res = search("""
                {"size":0,"query":%s,
                 "aggs":{"by_status":{"terms":{"field":"http_status","size":5,"order":{"_count":"desc"}}}}}
                """.formatted(baseQuery(minutes)));

        List<StatusCountResponse> result = new ArrayList<>();
        for (JsonNode bucket : res.path("aggregations").path("by_status").path("buckets")) {
            result.add(new StatusCountResponse(bucket.path("key").asInt(), bucket.path("doc_count").asLong()));
        }
        return result;
    }

    /** API 경로별 요청 수와 평균 응답시간 (평균이 느린 순서로 5개) */
    public List<PathStatResponse> pathStats(int minutes) {
        JsonNode res = search("""
                {"size":0,"query":%s,
                 "aggs":{"by_path":{
                   "terms":{"field":"http_path","size":5,"order":{"avg_ms":"desc"}},
                   "aggs":{"avg_ms":{"avg":{"field":"duration_ms"}}}}}}
                """.formatted(baseQuery(minutes)));

        List<PathStatResponse> result = new ArrayList<>();
        for (JsonNode bucket : res.path("aggregations").path("by_path").path("buckets")) {
            result.add(new PathStatResponse(
                    bucket.path("key").asText(),
                    bucket.path("doc_count").asLong(),
                    round1(bucket.path("avg_ms").path("value").asDouble())));
        }
        return result;
    }

    /** 시간 구간별 전체 요청 수와 에러(5xx) 수 */
    public List<TimelinePointResponse> timeline(int minutes) {
        String interval = minutes <= 60 ? "1m" : "5m";
        JsonNode res = search("""
                {"size":0,"query":%s,
                 "aggs":{"per_time":{
                   "date_histogram":{
                     "field":"@timestamp","fixed_interval":"%s","format":"HH:mm",
                     "time_zone":"Asia/Seoul","min_doc_count":0,
                     "extended_bounds":{"min":"now-%dm","max":"now"}},
                   "aggs":{"errors":{"filter":{"range":{"http_status":{"gte":500}}}}}}}}
                """.formatted(baseQuery(minutes), interval, minutes));

        List<TimelinePointResponse> result = new ArrayList<>();
        for (JsonNode bucket : res.path("aggregations").path("per_time").path("buckets")) {
            result.add(new TimelinePointResponse(
                    bucket.path("key_as_string").asText(),
                    bucket.path("doc_count").asLong(),
                    bucket.path("errors").path("doc_count").asLong()));
        }
        return result;
    }

    /** 최근 N분의 /api/ 접근 로그만 고르는 조건 (/api/stats 제외) */
    private String baseQuery(int minutes) {
        if (minutes < 1 || minutes > 1440) {
            throw new IllegalArgumentException("minutes는 1 ~ 1440 사이여야 합니다: " + minutes);
        }
        return """
                {"bool":{
                  "filter":[
                    {"term":{"logger_name":"access"}},
                    {"prefix":{"http_path":"/api/"}},
                    {"range":{"@timestamp":{"gte":"now-%dm"}}}],
                  "must_not":[{"prefix":{"http_path":"/api/stats"}}]}}
                """.formatted(minutes);
    }

    /**
     * JsonNode는 JSON을 트리 구조로 담는 Jackson 클래스입니다.
     * 집계 결과는 모양이 제각각이라 별도 클래스 없이 path()로 필요한 값만 꺼내려고 사용합니다.
     */
    private JsonNode search(String body) {
        try {
            JsonNode res = restClient.post()
                    .uri("/app-log-*/_search")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);
            return res == null ? MAPPER.createObjectNode() : res;
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                // 인덱스가 아직 없으면 빈 결과로 처리합니다.
                return MAPPER.createObjectNode();
            }
            log.error("Elasticsearch 조회 실패: {}", e.getResponseBodyAsString());
            throw new StatsUnavailableException(e);
        } catch (RestClientException e) {
            log.error("Elasticsearch에 연결할 수 없습니다", e);
            throw new StatsUnavailableException(e);
        }
    }

    private double round1(double value) {
        return Math.round(value * 10) / 10.0;
    }
}
