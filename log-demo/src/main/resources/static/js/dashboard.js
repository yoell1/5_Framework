// 로그 분석 화면: 통계 API를 호출해 숫자와 차트를 그립니다.

const charts = {};
const REFRESH_MS = 10000;

const $ = (id) => document.getElementById(id);

async function getJson(url) {
  const res = await fetch(url);
  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    throw new Error(body.error || "요청 실패 (" + res.status + ")");
  }
  return res.json();
}

// 차트가 이미 있으면 데이터만 바꾸고, 없으면 새로 만듭니다.
function draw(id, type, labels, datasets, options) {
  if (charts[id]) {
    charts[id].data.labels = labels;
    charts[id].data.datasets = datasets;
    charts[id].update();
    return;
  }
  charts[id] = new Chart($(id), {
    type: type,
    data: { labels: labels, datasets: datasets },
    options: Object.assign({ responsive: true, maintainAspectRatio: false }, options),
  });
}

// 축 이름(레이블) 설정을 만듭니다.
function axisTitle(text) {
  return { display: true, text: text };
}

function statusColor(status) {
  if (status >= 500) return "#e74c3c";
  if (status >= 400) return "#f39c12";
  return "#2ecc71";
}

async function load() {
  const minutes = $("minutes").value;
  const query = "?minutes=" + minutes;

  try {
    const [summary, status, paths, timeline] = await Promise.all([
      getJson("/api/stats/summary" + query),
      getJson("/api/stats/status" + query),
      getJson("/api/stats/paths" + query),
      getJson("/api/stats/timeline" + query),
    ]);

    $("total").textContent = summary.total.toLocaleString();
    $("errors").textContent = summary.errors.toLocaleString();
    $("errorRate").textContent = summary.errorRate + "%";
    $("avgMs").textContent = summary.avgMs + " ms";

    draw("timelineChart", "line", timeline.map((p) => p.time), [
      { label: "전체 요청", data: timeline.map((p) => p.total), borderColor: "#3498db", tension: 0.2 },
      { label: "에러(5xx)", data: timeline.map((p) => p.errors), borderColor: "#e74c3c", tension: 0.2 },
    ], { scales: {
      x: { title: axisTitle("시간 (시:분)") },
      y: { title: axisTitle("요청 수 (건)"), beginAtZero: true, ticks: { precision: 0 } },
    } });

    draw("statusChart", "bar", status.map((s) => String(s.status)), [
      { label: "요청 수", data: status.map((s) => s.count), backgroundColor: status.map((s) => statusColor(s.status)) },
    ], { plugins: { legend: { display: false } }, scales: {
      x: { title: axisTitle("HTTP 상태코드") },
      y: { title: axisTitle("요청 수 (건)"), beginAtZero: true, ticks: { precision: 0 } },
    } });

    draw("pathChart", "bar", paths.map((p) => p.path), [
      { label: "평균 응답시간(ms)", data: paths.map((p) => p.avgMs), backgroundColor: "#9b59b6" },
    ], { indexAxis: "y", plugins: { legend: { display: false } }, scales: {
      x: { title: axisTitle("평균 응답시간 (ms)"), beginAtZero: true },
      y: { title: axisTitle("API 경로") },
    } });

    $("message").textContent = "";
  } catch (e) {
    $("message").textContent = "통계를 불러오지 못했습니다: " + e.message;
  }
}

// 테스트용 요청을 20번 보내 로그를 만듭니다. (PowerShell 스크립트를 쓰지 않아도 됩니다)
async function sendTraffic() {
  const button = $("traffic");
  const targets = [
    "/api/hello", "/api/hello", "/api/random", "/api/random", "/api/random",
    "/api/slow", "/api/error", "/api/items/1", "/api/items/0", "/api/items/abc",
  ];

  button.disabled = true;
  button.textContent = "요청 보내는 중...";
  const requests = [];
  for (let i = 0; i < 20; i++) {
    const path = targets[Math.floor(Math.random() * targets.length)];
    requests.push(fetch(path).catch(() => {}));
  }
  await Promise.all(requests);

  // 로그가 Logstash를 거쳐 Elasticsearch에 반영될 시간을 잠시 기다립니다.
  setTimeout(() => {
    button.disabled = false;
    button.textContent = "테스트 요청 20회 보내기";
    load();
  }, 3000);
}

$("refresh").addEventListener("click", load);
$("minutes").addEventListener("change", load);
$("traffic").addEventListener("click", sendTraffic);

load();
setInterval(load, REFRESH_MS);
