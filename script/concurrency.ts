import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  scenarios: {
    random_load_test: {
      executor: "ramping-vus",
      stages: [
        { target: 20, duration: "20s" }, // 0초~20초 동안 VU를 0->5로 점진적 증가
      ],
    },
  },
  thresholds: {
    // 요청 응답 시간의 90퍼센타가 1000ms 이하, 95퍼센타가 800ms 이하ㅣ
    http_req_duration: ["p(90)<1000", "p(95)<800"],
    // check()가 90% 이상 성공해야 한다
    checks: ["rate>0.9"],
  },
};

// 테스트할 서버의 기본 URL
const baseUrl = "http://localhost:8080";

export default function () {
  // 1) 주문 생성 요청 (POST /api/orders)
  const orderPayload = JSON.stringify({
    numbers: [[1, 10, 11, 14, 15, 17]],
  });

  const order = http.post(`${baseUrl}/api/orders`, orderPayload, {
    headers: { "Content-Type": "application/json" },
  });

  check(order, {
    "주문 생성 요청 성공": (r) => r.status === 200,
  });

  let orderData = order.json().data || {};
  let lottoPublishId = orderData.lottoPublishId;

  const paymentPayload = JSON.stringify({
    lottoPublishId: orderData.lottoPublishId,
    purchaseHttpRequest: {
      purchaseType: "CARD",
      currency: "KRW",
      amount: 1000,
      orderId: orderData.orderId,
      paymentKey: orderData.orderId,
    },
  });

  const requests = [];
  for (let i = 0; i < 5; i++) {
    requests.push([
      "POST",
      `${baseUrl}/api/tickets`,
      paymentPayload,
      {
        headers: {
          "Content-Type": "application/json",
          Authorization:
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhOTU5ODFiNS1jMDEyLTRmZTYtOTBhNC1lOTE3YjI5NTU5NDIiLCJpYXQiOjE3NDA5MzEyNDgsImV4cCI6MTc0MDk0NzI0OH0.vs6MDnyOXIR6QPzYqYMMie0Vvi--RkJbxGDUkyHc0eA",
        },
      },
    ]);
  }

  const responses = http.batch(requests);

  // 응답 상태별로 개수를 센다
  let successCount = 0;
  let badRequestCount = 0;
  let concurrencyCount = 0;

  responses.forEach((res, idx) => {
    if (res.status === 200) {
      successCount++;
    } else if (res.status === 400) {
      badRequestCount++;
    } else if (res.status === 500){
        concurrencyCount++;
    }
  });
  console.log(successCount+"\t"+badRequestCount+"\t"+concurrencyCount);

  // 원하는 조건: "1개의 요청만 200, 나머지 4개는 400"
  check({ successCount, badRequestCount }, {
    "exactly 1 success and 4 bad requests": (obj) =>
      obj.successCount === 1 && obj.badRequestCount === 4,
  });
  responses.forEach((res) => {
    check(res, {
      "티켓 구매 요청 2초 이내 완료": (r) => r.timings.duration < 2000,
    });
  });

  sleep(1);
}
