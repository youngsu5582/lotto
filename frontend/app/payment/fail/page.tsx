"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function PaymentFail() {
  const router = useRouter();

  useEffect(() => {
    const timer = setTimeout(() => {
      router.push('/payment');
    }, 3000);

    return () => clearTimeout(timer);
  }, [router]);

  return (
    <div className="min-h-screen bg-neutral-900 flex items-center justify-center">
      <div className="text-center">
        <h1 className="text-3xl font-bold text-white mb-4">결제 실패</h1>
        <p className="text-neutral-400">잠시 후 결제 페이지로 돌아갑니다...</p>
      </div>
    </div>
  );
} 