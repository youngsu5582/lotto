"use client";

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { ticketApi } from '../../services';
import { Bill, BillsResponse, TicketListResponse } from '../../types/ticket';

export default function MyTickets() {
  const router = useRouter();
  const [bills, setBills] = useState<Bill[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      router.push('/');
      return;
    }

    loadBills();
  }, [router]);

  const loadBills = async () => {
    try {
      setLoading(true);
      const response : BillsResponse = await ticketApi.getMyTickets() as BillsResponse;
      setBills(response.data.response);
    } catch (error) {
      setError('구매 내역을 불러오는데 실패했습니다.');
      console.error('Failed to load bills:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (billId: number) => {
    if (!confirm('정말로 이 구매를 취소하시겠습니까?')) {
      return;
    }

    try {
      await ticketApi.cancelTicket(billId);
      await loadBills(); // 목록 새로고침
      alert('구매가 취소되었습니다.');
    } catch (error) {
      alert('구매 취소에 실패했습니다.');
      console.error('Failed to cancel bill:', error);
    }
  };

  const getNumberColor = (num: number) => {
    if (num <= 10) return 'bg-yellow-500';
    if (num <= 20) return 'bg-blue-500';
    if (num <= 30) return 'bg-red-500';
    if (num <= 40) return 'bg-green-500';
    return 'bg-purple-500';
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-neutral-900 p-8">
        <div className="max-w-4xl mx-auto">
          <div className="text-white text-center">로딩 중...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-neutral-900 p-8">
        <div className="max-w-4xl mx-auto">
          <div className="text-red-500 text-center">{error}</div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-neutral-900 p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold text-white mb-8">내 구매 내역</h1>
        
        {bills.length === 0 ? (
          <div className="text-neutral-400 text-center">
            구매 내역이 없습니다.
          </div>
        ) : (
          <div className="space-y-6">
            {bills.map((bill) => (
              <div
                key={bill.billId}
                className="bg-neutral-800 p-6 rounded-lg border border-neutral-700"
              >
                <div className="flex justify-between items-start mb-6">
                  <div className="space-y-2">
                    <div className="flex items-center gap-3">
                      <span className="text-white font-semibold">
                        {bill.publishResponse.lottoRoundInfo.round}회차
                      </span>
                      <span className={`px-3 py-1 rounded-full text-sm ${
                        bill.publishResponse.lottoRoundInfo.status === 'ONGOING' 
                          ? 'bg-green-600 text-white' 
                          : 'bg-neutral-700 text-neutral-400'
                      }`}>
                        {bill.publishResponse.lottoRoundInfo.status}
                      </span>
                      <span className={`px-3 py-1 rounded-full text-sm ${
                        bill.publishResponse.status === 'CANCELED'
                          ? 'bg-red-600 text-white'
                          : 'bg-blue-600 text-white'
                      }`}>
                        {bill.publishResponse.status === 'CANCELED' ? '취소됨' : '구매완료'}
                      </span>
                    </div>
                    <div className="text-neutral-400 text-sm">
                      주문번호: {bill.paymentResponse.id}
                    </div>
                    <div className="text-neutral-400 text-sm">
                      발행일: {new Date(bill.publishResponse.issuedAt).toLocaleString()}
                    </div>
                    <div className="text-white font-medium">
                      {bill.paymentResponse.amount.toLocaleString()}원
                    </div>
                  </div>
                  {bill.publishResponse.status !== 'CANCELED' && (
                    <button
                      onClick={() => handleCancel(bill.billId)}
                      className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
                    >
                      취소
                    </button>
                  )}
                </div>

                <div className="space-y-3">
                  <div className="text-sm text-neutral-400">선택한 번호</div>
                  {bill.publishResponse.lottoes.map((numbers, idx) => (
                    <div 
                      key={idx} 
                      className={`p-4 bg-neutral-700 rounded-lg flex flex-wrap gap-2 ${
                        bill.publishResponse.status === 'CANCELED' ? 'opacity-50' : ''
                      }`}
                    >
                      {numbers.map((num) => (
                        <span
                          key={num}
                          className={`inline-flex items-center justify-center w-9 h-9 rounded-full text-white font-semibold ${getNumberColor(num)} transition-transform hover:scale-110`}
                        >
                          {num}
                        </span>
                      ))}
                    </div>
                  ))}
                </div>

                <div className="mt-4 text-sm text-neutral-400">
                  추첨일: {new Date(bill.publishResponse.lottoRoundInfo.drawDate).toLocaleDateString()}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
} 