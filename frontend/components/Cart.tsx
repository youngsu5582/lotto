"use client";

import { useState } from 'react';
import { IoClose } from 'react-icons/io5';
import { LottoTickets } from '../types/lotto';
import { useLottoRoundInfo } from '../hooks/useLottoRoundInfo';
import { StatsModal } from './StatsModal';
import { lottoDrawApi } from "../services";
import { LottoDrawPreviewResponse } from '@/types/lotto-draw';

/** 서버 응답 전체 구조 */
interface CartProps {
  tickets: LottoTickets;
  onRemoveTicket: (index: number) => void;
  onPurchase: () => void;
}

export default function Cart({ tickets, onRemoveTicket, onPurchase }: CartProps) {
  const { roundInfo } = useLottoRoundInfo();
  const isDisabled = !roundInfo || roundInfo.status !== 'ONGOING';

  // 모달 상태
  const [statsModalOpen, setStatsModalOpen] = useState(false);
  const [statsData, setStatsData] = useState<LottoDrawPrivewResponse | null>(null);

  /** 티켓 전체 번호를 QueryParam으로 보내기 */
  const fetchTicketStats = async (ticket: number[]) => {
    try {
      const response = await lottoDrawApi.getLottoDrawPreview(ticket);
      setStatsData(response.data);
      setStatsModalOpen(true);   
    } catch (error) {
      console.error(error);
      alert('통계 조회에 실패했습니다.');
    }
  };

  return (
    <div className="bg-neutral-800 p-6 rounded-lg shadow-lg border border-neutral-700">
      <h2 className="text-xl font-semibold mb-4 text-white">장바구니</h2>

      {tickets.map((ticket, idx) => (
        <div key={idx} className="mb-2 p-3 bg-neutral-700 rounded-lg">
          <div className="flex justify-between items-center">
            <div>
              {ticket.map((num) => (
                <span
                  key={num}
                  className="inline-block w-8 h-8 mx-1 text-center leading-8 bg-blue-500 text-white rounded-full font-semibold"
                >
                  {num}
                </span>
              ))}
            </div>
            <div className="flex items-center">
              {/* 티켓 통계 버튼 (숫자 배열 전송) */}
              <button
                onClick={() => fetchTicketStats(ticket)}
                className="mr-2 px-2 py-1 text-sm bg-neutral-500 text-white rounded hover:bg-neutral-400"
              >
                ?
              </button>
              {/* 티켓 제거 버튼 -> X 아이콘 */}
              <button
                onClick={() => onRemoveTicket(idx)}
                className="p-2 text-sm bg-red-500 text-white rounded hover:bg-red-600"
              >
                <IoClose size={16} />
              </button>
            </div>
          </div>
        </div>
      ))}

      {tickets.length === 0 ? (
        <p className="text-neutral-400 text-center">장바구니가 비어있습니다.</p>
      ) : (
        <>
          <button
            onClick={onPurchase}
            disabled={isDisabled}
            className={`w-full mt-4 px-6 py-2 rounded-lg transition-colors ${
              isDisabled 
                ? 'bg-neutral-600 text-neutral-400 cursor-not-allowed'
                : 'bg-green-600 text-white hover:bg-green-700'
            }`}
          >
            {isDisabled ? '판매 종료' : '구매하기'}
          </button>
          {isDisabled && roundInfo && (
            <p className="text-red-500 text-sm mt-2 text-center">
              {roundInfo.status === 'COMPLETED' ? '추첨이 완료되었습니다.' : '판매가 종료되었습니다.'}
            </p>
          )}
        </>
      )}

      {/* 통계 모달 */}
      <StatsModal
        isOpen={statsModalOpen}
        onClose={() => setStatsModalOpen(false)}
        data={statsData || undefined}
      />
    </div>
  );
}
