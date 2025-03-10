"use client";

import { LottoDrawPrivewResponse } from '@/types/lotto-draw';
import React from 'react';
import { IoClose } from 'react-icons/io5';

interface StatsModalProps {
  isOpen: boolean;
  onClose: () => void;
  data?: LottoDrawPrivewResponse;
}

export function StatsModal({ isOpen, onClose, data }: StatsModalProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div className="relative w-96 bg-neutral-800 text-white p-6 rounded-lg shadow-lg">
        {/* 닫기 버튼 */}
        <button 
          className="absolute top-3 right-3 text-neutral-300 hover:text-white"
          onClick={onClose}
        >
          <IoClose size={20} />
        </button>

        <h2 className="text-xl font-bold mb-4">티켓 통계</h2>

        {data && (
          <>
            {/* 출현 횟수 섹션 */}
            <div className="mb-6">
              <h3 className="font-semibold mb-2">출현 횟수</h3>
              <div className="space-y-1">
                {data.lottoNumberCountResponse.map(item => (
                  <div key={item.number} className="flex justify-between">
                    <span>번호 {item.number}</span>
                    <span>{item.count}회</span>
                  </div>
                ))}
              </div>
            </div>

            {/* 최근 당첨 결과 섹션 */}
            <div>
              <h3 className="font-semibold mb-2">최근 당첨 결과</h3>
              <div className="space-y-4">
                {data.lottoResultResponse.map(result => {
                  const { date, numbers, bonus } = result.lottoDrawResponse;
                  const { matchCount, bonusMatch } = result.drawResultResponse;

                  return (
                    <div key={result.round} className="p-3 bg-neutral-700 rounded-md">
                      {/* 회차 / 날짜 */}
                      <div className="flex justify-between items-center mb-2">
                        <span className="font-semibold">{result.round}회차</span>
                        <span className="text-sm text-neutral-400">{date}</span>
                      </div>

                      {/* 메인 번호 + 보너스 번호 */}
                      <div className="flex flex-wrap gap-2">
                        {numbers.map((num, idx) => (
                          <span
                            key={idx}
                            className="inline-block w-8 h-8 text-center leading-8 bg-blue-500 text-white rounded-full font-semibold"
                          >
                            {num}
                          </span>
                        ))}
                        {/* 보너스 번호는 색상 다르게 */}
                        <span className="inline-block w-8 h-8 text-center leading-8 bg-red-500 text-white rounded-full font-semibold">
                          B:{bonus}
                        </span>
                      </div>

                      {/* 일치 결과 */}
                      <div className="mt-2 text-sm">
                        <span className="font-semibold text-yellow-400">
                          {matchCount}개 일치
                        </span>
                        {bonusMatch && (
                          <span className="ml-1 text-pink-400 font-semibold">
                            + 보너스 일치!
                          </span>
                        )}
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
