# log-demo
> 스프링 서버에서 기록된 로그 통계 데모 서비스

---

## 사전 준비
1. `elasticsearch/index-template.md` 파일을 참고하여 인덱스를 준비해주세요.
2. `logstash/logstash.conf` 파일을 로그 스태시 폴더의 config 폴더에 복사한 후, `pipelines.yml` 파일에 설정을 해주세요.
3. 엘라스틱 서치, 로그스태시를 실행한 후 스프링 서버를 실행하여 확인해주세요.

---

## 확인
* http://localhost:8080/ 페이지에 접속하여 [테스트 요청 20회 보내기]를 클릭하면 로그 집계 결과를 확인할 수 있습니다.
