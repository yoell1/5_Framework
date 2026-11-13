# 인덱스 템플릿

Kibana Dev Tools에 붙여 넣어 실행합니다. 템플릿은 새로 만들어지는 인덱스에만 적용되므로 로그를 보내기 전에 먼저 등록합니다.
이미 `app-log-*` 인덱스가 있으면 `DELETE app-log-*`로 지운 뒤 로그를 다시 보냅니다.

## 등록

```
PUT _template/app-log
{
  "index_patterns": ["app-log-*"],
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "properties": {
      "@timestamp":  { "type": "date" },
      "level":       { "type": "keyword" },
      "logger_name": { "type": "keyword" },
      "thread_name": { "type": "keyword" },
      "app":         { "type": "keyword" },
      "traceId":     { "type": "keyword" },
      "message":     { "type": "text" },
      "http_method": { "type": "keyword" },
      "http_path":   { "type": "keyword" },
      "http_status": { "type": "integer" },
      "duration_ms": { "type": "long" },
      "stack_trace": { "type": "text" }
    }
  }
}
```

## 확인

```
GET _template/app-log
```
