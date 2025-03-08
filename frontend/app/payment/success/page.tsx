"use client";

import { Suspense } from "react";
import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { paymentApi } from "../../../services";

interface PaymentStatus {
  status: 'loading' | 'success' | 'error';
  message?: string;
}

function PaymentContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [paymentStatus, setPaymentStatus] = useState<PaymentStatus>({ status: 'loading' });

  useEffect(() => {
    const verifyPayment = async () => {
      try {
        const paymentKey = searchParams.get('paymentKey');
        const orderId = searchParams.get('orderId');
        const amount = searchParams.get('amount');
        const lottoPublishId = localStorage.getItem('lottoPublishId');

        if (!paymentKey || !orderId || !amount || !lottoPublishId) {
          throw new Error('Missing payment parameters');
        }

        await paymentApi.verifyPayment({
          paymentKey,
          orderId,
          amount: Number(amount),
          lottoPublishId: Number(lottoPublishId)
        });

        setPaymentStatus({ status: 'success' });
        
        // 결제 완료 후 로컬 스토리지 정리
        localStorage.removeItem('lottoPublishId');
        localStorage.removeItem('lottoTickets');
        
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
  );
}

export default function PaymentSuccess() {
  return (
    <div className="min-h-screen bg-neutral-900 flex items-center justify-center">
      <Suspense fallback={
        <div className="text-center">
          <h1 className="text-3xl font-bold text-white mb-4">로딩 중...</h1>
        </div>
      }>
        <PaymentContent />
      </Suspense>
    </div>
  );
} 