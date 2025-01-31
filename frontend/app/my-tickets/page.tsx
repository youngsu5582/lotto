"use client";

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { ticketApi } from '../../services';
import { LottoTicketHistory } from '../../types/ticket';

export default function MyTickets() {
  const router = useRouter();
  const [tickets, setTickets] = useState<LottoTicketHistory[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      router.push('/');
      return;
    }

    loadTickets();
  }, [router]);

  const loadTickets = async () => {
    try {
      setLoading(true);
      const response = await ticketApi.getMyTickets();
      setTickets(response.tickets);
    } catch (error) {
      setError('구매 내역을 불러오는데 실패했습니다.');
      console.error('Failed to load tickets:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (ticketId: number) => {
    if (!confirm('정말로 이 구매를 취소하시겠습니까?')) {
      return;
    }

    try {
      await ticketApi.cancelTicket(ticketId);
      await loadTickets(); // 목록 새로고침
      alert('구매가 취소되었습니다.');
    } catch (error) {
      alert('구매 취소에 실패했습니다.');
      console.error('Failed to cancel ticket:', error);
    }
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
        
        {tickets.length === 0 ? (
          <div className="text-neutral-400 text-center">
            구매 내역이 없습니다.
          </div>
        ) : (
          <div className="space-y-4">
            {tickets.map((ticket) => (
              <div
                key={ticket.id}
                className="bg-neutral-800 p-6 rounded-lg border border-neutral-700"
              >
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <div className="text-white font-semibold">
                      주문번호: {ticket.orderId}
                    </div>
                    <div className="text-neutral-400 text-sm">
                      구매일: {new Date(ticket.purchaseDate).toLocaleString()}
                    </div>
                    <div className="text-neutral-400 text-sm">
                      금액: {ticket.amount.toLocaleString()}원
                    </div>
                  </div>
                  {ticket.status === 'ACTIVE' && (
                    <button
                      onClick={() => handleCancel(ticket.id)}
                      className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
                    >
                      취소
                    </button>
                  )}
                  {ticket.status === 'CANCELLED' && (
                    <span className="text-red-500">취소됨</span>
                  )}
                </div>

                <div className="space-y-2">
                  {ticket.numbers.map((numbers, idx) => (
                    <div key={idx} className="p-3 bg-neutral-700 rounded-lg">
                      {numbers.map((num) => (
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
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
} 