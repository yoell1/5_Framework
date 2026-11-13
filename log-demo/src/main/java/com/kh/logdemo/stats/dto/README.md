# stats/dto

로그 통계 API의 응답 데이터입니다. 모두 record로 만들었습니다.

- StatsSummaryResponse: 요약 통계. total, errors, errorRate, avgMs
- StatusCountResponse: 상태코드별 요청 수. status, count
- PathStatResponse: API 경로별 통계. path, count, avgMs
- TimelinePointResponse: 시간 구간별 통계. time, total, errors
