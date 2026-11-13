# Spring 없이 Logstash 파이프라인만 먼저 확인하는 스크립트.
# JSON 한 줄을 Logstash TCP 5000 포트로 보냅니다.
#
# 실행:  powershell -ExecutionPolicy Bypass -File .\scripts\send-test-log.ps1

$json = '{"level":"INFO","logger_name":"access","message":"GET /api/test 200 123ms","traceId":"manualtest0001","http_method":"GET","http_path":"/api/test","http_status":200,"duration_ms":123,"app":"log-demo"}'

$client = New-Object System.Net.Sockets.TcpClient("localhost", 5000)
$writer = New-Object System.IO.StreamWriter($client.GetStream())
$writer.WriteLine($json)   # json_lines 코덱은 줄바꿈으로 한 건을 구분합니다
$writer.Flush()
$writer.Close()
$client.Close()

Write-Host "전송 완료. Logstash 콘솔에 이벤트가 출력되는지 확인하세요."
