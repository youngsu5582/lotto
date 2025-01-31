"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Script from "next/script";
import { LottoTickets } from "../types/lotto";

declare global {
  interface Window {
    PaymentWidget: any;
  }
}

export default function Payment() {
  const router = useRouter();
  const [tickets, setTickets] = useState<LottoTickets>([]);
  const [totalAmount, setTotalAmount] = useState(0);
  const [paymentWidget, setPaymentWidget] = useState<any>(null);

  useEffect(() => {
    // localStorage에서 티켓 정보 가져오기
    const savedTickets = localStorage.getItem('lottoTickets');
    if (!savedTickets) {
      // 티켓 정보가 없으면 홈으로 리다이렉트
      router.push('/');
      return;
    }
    const parsedTickets = JSON.parse(savedTickets);
    setTickets(parsedTickets);
    setTotalAmount(parsedTickets.length * 1000); // 각 티켓당 1000원

    // 토스 페이먼츠 위젯 초기화
    const clientKey = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY;
    const paymentWidget = window.PaymentWidget(clientKey);
    setPaymentWidget(paymentWidget);
  }, [router]);

  const handlePayment = async () => {
    try {
      await paymentWidget.requestPayment({
        amount: totalAmount,
        orderId: `${Date.now()}`,
        orderName: `로또 티켓 ${tickets.length}장`,
        customerName: "고객",
        successUrl: `${window.location.origin}/payment/success`,
        failUrl: `${window.location.origin}/payment/fail`,
      });
    } catch (error) {
        const errorMessage = error instanceof Error ? error.message : '알 수 없는 오류가 발생했습니다.';
        alert(`결제 중 오류가 발생했습니다: ${errorMessage}`);
    }
    }
  };

  return (
    <>
      <Script 
        src="https://js.tosspayments.com/v1/payment-widget"
        strategy="beforeInteractive"
      />
      
      <div className="min-h-screen bg-neutral-900">
        <div className="max-w-540 mx-auto p-8">
          <h1 className="text-3xl font-bold text-white mb-4">결제 확인</h1>
          <p className="text-neutral-400 mb-8">선택하신 로또 번호를 확인해주세요</p>

          <div className="bg-neutral-800 p-6 rounded-lg shadow-lg border border-neutral-700 mb-6">
            {tickets.map((ticket, idx) => (
              <div key={idx} className="mb-2 p-3 bg-neutral-700 rounded-lg">
                {ticket.map((num) => (
                  <span
                    key={num}
                    className="inline-block w-8 h-8 mx-1 text-center leading-8 bg-blue-500 text-white rounded-full font-semibold"
                  >
                    {num}
                  </span>
                ))}
              </div>
            ))}
          </div>

          <div className="flex justify-between text-white mb-6 p-4 bg-neutral-800 rounded-lg">
            <span>총 결제금액</span>
            <span>{totalAmount.toLocaleString()}원</span>
          </div>

          <div className="space-y-4">
            <button 
              className="w-full px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              onClick={handlePayment}
            >
              결제하기
            </button>
            <button 
              className="w-full px-6 py-3 bg-neutral-700 text-white rounded-lg hover:bg-neutral-600 transition-colors"
              onClick={() => router.push('/')}
            >
              취소
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
