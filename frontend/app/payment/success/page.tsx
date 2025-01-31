"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { apiService } from "../../../services/api";
import { LottoTickets } from "../../../types/lotto";

interface PaymentStatus {
  status: 'loading' | 'success' | 'error';
  message?: string;
}

export default function PaymentSuccess() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [paymentStatus, setPaymentStatus] = useState<PaymentStatus>({ status: 'loading' });

  useEffect(() => {
    const verifyPayment = async () => {
      try {
        // URL에서 결제 정보 파라미터 추출
        const paymentKey = searchParams.get('paymentKey');
        const orderId = searchParams.get('orderId');
        const amount = searchParams.get('amount');

        // localStorage에서 로또 번호 가져오기
        const savedTickets = localStorage.getItem('lottoTickets');
        if (!paymentKey || !orderId || !amount || !savedTickets) {
          throw new Error('Missing payment parameters or lotto tickets');
        }

        const tickets = JSON.parse(savedTickets) as LottoTickets;
        
        console.log('Payment Info:', { paymentKey, orderId, amount });
        console.log('Lotto Tickets:', tickets);

        // 결제 검증 요청
        await apiService.verifyPayment(
          {
            paymentKey,
            orderId,
            amount: Number(amount),
          },
          tickets
        );

        // 결제 검증 성공
        setPaymentStatus({ status: 'success' });
        
        // 결제 완료 후 로컬 스토리지 정리
        localStorage.removeItem('lottoTickets');
        
        // 3초 후 홈으로 이동
        setTimeout(() => {
          router.push('/');
        }, 3000);
      } catch (error) {
        console.error('Payment verification failed:', error);
        setPaymentStatus({ 
          status: 'error', 
          message: '결제 검증에 실패했습니다. 고객센터로 문의해주세요.' 
        });
      }
    };

    verifyPayment();
  }, [router, searchParams]);

  return (
    <div className="min-h-screen bg-neutral-900 flex items-center justify-center">
      <div className="text-center">
        {paymentStatus.status === 'loading' && (
          <h1 className="text-3xl font-bold text-white mb-4">결제 확인 중...</h1>
        )}
        
        {paymentStatus.status === 'success' && (
          <>
            <h1 className="text-3xl font-bold text-white mb-4">결제 성공!</h1>
            <p className="text-neutral-400">잠시 후 메인 페이지로 이동합니다...</p>
          </>
        )}

        {paymentStatus.status === 'error' && (
          <>
            <h1 className="text-3xl font-bold text-red-500 mb-4">결제 확인 실패</h1>
            <p className="text-neutral-400">{paymentStatus.message}</p>
            <button
              className="mt-4 px-6 py-2 bg-neutral-700 text-white rounded-lg hover:bg-neutral-600"
              onClick={() => router.push('/')}
            >
              홈으로 돌아가기
            </button>
          </>
        )}
      </div>
    </div>
  );
} 