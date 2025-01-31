"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Script from "next/script";
import { LottoTickets } from "../../types/lotto";
import { nanoid } from "nanoid";
import { TOSS_CLIENT_KEY } from '../../config';
import { apiService } from "../../services/api";

declare global {
  interface Window {
    TossPayments: (clientKey: string) => any;
  }
}

export default function Payment() {
  const router = useRouter();
  const [tickets, setTickets] = useState<LottoTickets>([]);
  const [totalAmount, setTotalAmount] = useState(0);
  const [isProcessing, setIsProcessing] = useState(false);
  const [isScriptLoaded, setIsScriptLoaded] = useState(false);

  useEffect(() => {
    const savedTickets = localStorage.getItem('lottoTickets');
    if (!savedTickets) {
      router.push('/');
      return;
    }
    const parsedTickets = JSON.parse(savedTickets);
    setTickets(parsedTickets);
    setTotalAmount(parsedTickets.length * 1000);
  }, [router]);

  const handlePayment = async () => {
    if (isProcessing || !isScriptLoaded) return;
    setIsProcessing(true);

    try {
      const orderId = nanoid();
      
      // 결제 창을 열기 전에 백그라운드에서 임시 주문 생성 요청
      const temporaryOrderPromise = apiService.createTemporaryOrder({
        orderId,
        amount: totalAmount,
        numbers: tickets,
      });

      // 토스페이먼츠 결제 위젯 초기화
      const tossPayments = window.TossPayments(TOSS_CLIENT_KEY);

      // 두 작업을 동시에 실행
      const [temporaryOrderResult] = await Promise.all([
        temporaryOrderPromise,
        // 결제 창 열기 (Promise를 반환하지 않음)
        tossPayments.requestPayment('카드', {
          amount: totalAmount,
          orderId,
          orderName: `로또 티켓 ${tickets.length}장`,
          customerName: localStorage.getItem('userName') || "고객",
          successUrl: `${window.location.origin}/payment/success`,
          failUrl: `${window.location.origin}/payment/fail`,
        })
      ]).catch(error => {
        // 임시 주문 생성이 실패하더라도 결제 창은 이미 열렸을 수 있음
        throw error;
      });

      // 임시 주문 생성 결과 확인
      if (!temporaryOrderResult.success) {
        throw new Error(temporaryOrderResult.message || '임시 주문 생성 실패');
      }

    } catch (error: any) {
      if (error.code === "USER_CANCEL") {
        alert("결제가 취소되었습니다.");
      } else if (error.code === "INVALID_CARD_COMPANY") {
        alert("유효하지 않은 카드입니다.");
      } else {
        alert("결제 중 오류가 발생했습니다.");
        console.error('결제 실패:', error);
      }
    } finally {
      setIsProcessing(false);
    }
  };

  return (
    <>
      <Script 
        src="https://js.tosspayments.com/v1/payment"
        strategy="afterInteractive"
        onLoad={() => setIsScriptLoaded(true)}
        onError={() => {
          alert('결제 시스템을 불러오는데 실패했습니다. 페이지를 새로고침 해주세요.');
          console.error('Toss Payments 스크립트 로드 실패');
        }}
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
              className="w-full px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors disabled:bg-neutral-700"
              onClick={handlePayment}
              disabled={isProcessing || !isScriptLoaded}
            >
              {isProcessing ? "처리 중..." : "결제하기"}
            </button>
            <button 
              className="w-full px-6 py-3 bg-neutral-700 text-white rounded-lg hover:bg-neutral-600 transition-colors"
              onClick={() => router.push('/')}
              disabled={isProcessing}
            >
              취소
            </button>
          </div>
        </div>
      </div>
    </>
  );
} 