import http from "k6/http";
import { check, sleep } from "k6";

const baseUrl = "http://localhost:3000"

export const options = {
  scenarios: {
    random_load_test: {
      executor: "ramping-vus",
      stages: [
        { target: 5, duration: "20s" }, // 초당 0~5회 요청
      ],
    },
  },
  thresholds: {
    http_req_duration: ["p(90)<1000", "p(95)<800"],
    checks: ["rate>0.9"],
  },
};

export default function () {
  const baseUrl = "http://localhost:3000";
  const now = new Date(Date.now());

  // 년-월-일 시:분:초 포맷으로 변환
  const formattedDate = now.getFullYear() + '-'
                      + (now.getMonth() + 1).toString().padStart(2, '0') + '-'
                      + now.getDate().toString().padStart(2, '0') + ' '
                      + now.getHours().toString().padStart(2, '0') + ':'
                      + now.getMinutes().toString().padStart(2, '0') + ':'
                      + now.getSeconds().toString().padStart(2, '0');

  console.log(formattedDate  + "  :  " + __VU);

  let response = http.get(`${baseUrl}`);
  check(response, {
    "메인 페이지 로드 성공": (r) => r.status === 200,
    "응답 시간이 3000ms 미만인지": (r) => r.timings.duration < 3000,
  });

  sleep(0.5);
}

