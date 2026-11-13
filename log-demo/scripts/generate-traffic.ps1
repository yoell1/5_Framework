# 앱(localhost:8080)에 요청을 반복해서 보내 분석할 로그를 만듭니다.
#
# 실행:  powershell -ExecutionPolicy Bypass -File .\scripts\generate-traffic.ps1
# 기본 200회. 횟수를 바꾸려면:  ... -File .\scripts\generate-traffic.ps1 -Count 500

param([int]$Count = 200)

$base = "http://localhost:8080"
$paths = @(
    "/api/hello", "/api/hello", "/api/hello",
    "/api/random", "/api/random", "/api/random", "/api/random",
    "/api/slow",
    "/api/error",
    "/api/items/1", "/api/items/42", "/api/items/0", "/api/items/abc"
)

for ($i = 1; $i -le $Count; $i++) {
    $path = $paths | Get-Random
    try {
        Invoke-WebRequest -Uri ($base + $path) -UseBasicParsing | Out-Null
    } catch {
        # 400, 404, 500 응답은 예외로 잡힙니다. 의도된 에러이므로 무시합니다.
    }
    if ($i % 20 -eq 0) { Write-Host "$i / $Count 요청 완료" }
    Start-Sleep -Milliseconds (Get-Random -Minimum 50 -Maximum 300)
}

Write-Host "완료. Kibana Discover에서 최근 15분 로그를 확인하세요."
