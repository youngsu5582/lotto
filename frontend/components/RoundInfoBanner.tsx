import { useEffect, useState } from 'react';
import { LottoRoundInfo } from '../types/lotto';
import { lottoApi } from '../services';
import { motion } from 'framer-motion';
import { useLottoRoundInfo } from '../hooks/useLottoRoundInfo';

export default function RoundInfoBanner() {
  const { roundInfo, loading, error } = useLottoRoundInfo(300000); // 5분마다 갱신
  const [timeLeft, setTimeLeft] = useState<string>('');

  const calculateTimeLeft = () => {
    if (!roundInfo) return;

    const now = new Date();
    const endDate = new Date(roundInfo.endDate);
    const diff = endDate.getTime() - now.getTime();

    if (diff <= 0) {
      setTimeLeft('마감되었습니다');
      return;
    }

    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

    setTimeLeft(`${days}일 ${hours}시간 ${minutes}분`);
  };

  useEffect(() => {
    if (roundInfo) {
      calculateTimeLeft();
      const intervalId = setInterval(calculateTimeLeft, 60000);
      return () => clearInterval(intervalId);
    }
  }, [roundInfo]);

  if (!roundInfo) return null;

  return (
    <motion.div 
      initial={{ opacity: 0, y: -20 }}
      animate={{ opacity: 1, y: 0 }}
      className="bg-neutral-900 border-b border-neutral-700"
    >
      <div className="max-w-7xl mx-auto px-4 py-2">
        <div className="flex justify-between items-center text-sm">
          <div className="flex items-center space-x-3">
            <span className="text-white font-bold">
              {roundInfo.round}회차
            </span>
            <span className={`px-2 py-0.5 rounded-full text-xs ${
              roundInfo.status === 'ONGOING' 
                ? 'bg-blue-600 text-white' 
                : 'bg-neutral-700 text-neutral-400'
            }`}>
              {roundInfo.status === 'ONGOING' ? '판매중' : roundInfo.status}
            </span>
          </div>
          <div className="flex items-center space-x-4">
            <div className="text-neutral-400">
              마감: {new Date(roundInfo.endDate).toLocaleString()}
            </div>
            <div className="flex items-center">
              <span className="text-neutral-400 mr-2">남은 시간:</span>
              <motion.span 
                key={timeLeft}
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                className="text-blue-400 font-medium"
              >
                {timeLeft}
              </motion.span>
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
} 