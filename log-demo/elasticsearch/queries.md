# 검색과 집계 연습 쿼리

Kibana Dev Tools에 붙여 넣어 실행합니다.

## 1. 최근 로그 10건

```
GET app-log-*/_search
{
  "size": 10,
  "sort": [{ "@timestamp": "desc" }]
}
```

## 2. ERROR 로그만

```
GET app-log-*/_search
{
  "query": { "term": { "level": "ERROR" } }
}
```

## 3. 같은 요청의 로그 모아 보기

traceId 값은 실제 값으로 바꿉니다.

```
GET app-log-*/_search
{
  "query": { "term": { "traceId": "여기에-traceId" } },
  "sort": [{ "@timestamp": "asc" }]
}
```

## 4. 1초 이상 걸린 요청

```
GET app-log-*/_search
{
  "query": {
    "bool": {
      "filter": [
        { "term":  { "logger_name": "access" } },
        { "range": { "duration_ms": { "gte": 1000 } } }
      ]
    }
  }
}
```

## 5. 상태코드별 요청 수

```
GET app-log-*/_search
{
  "size": 0,
  "query": { "term": { "logger_name": "access" } },
  "aggs": {
    "by_status": { "terms": { "field": "http_status" } }
  }
}
```

## 6. API 경로별 평균 응답시간

```
GET app-log-*/_search
{
  "size": 0,
  "query": { "term": { "logger_name": "access" } },
  "aggs": {
    "by_path": {
      "terms": { "field": "http_path" },
      "aggs": { "avg_ms": { "avg": { "field": "duration_ms" } } }
    }
  }
}
```

## 7. 분 단위 요청 수 추이

```
GET app-log-*/_search
{
  "size": 0,
  "query": { "term": { "logger_name": "access" } },
  "aggs": {
    "per_minute": {
      "date_histogram": { "field": "@timestamp", "fixed_interval": "1m" }
    }
  }
}
```

## 8. 5xx 에러 수

에러율 계산에 씁니다.

```
GET app-log-*/_count
{
  "query": {
    "bool": {
      "filter": [
        { "term":  { "logger_name": "access" } },
        { "range": { "http_status": { "gte": 500 } } }
      ]
    }
  }
}
```
