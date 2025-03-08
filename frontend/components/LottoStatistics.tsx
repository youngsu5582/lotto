import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import type { LottoStatistics } from '../types/lotto';
import { lottoApi } from '../services';

export default function LottoStatistics() {
  const [statistics, setStatistics] = useState<LottoStatistics | null>(null);
  const [loading, setLoading] = useState(true);
  const [hoveredCard, setHoveredCard] = useState<number | null>(null);

  useEffect(() => {
    const fetchStatistics = async () => {
      try {
        const response = await lottoApi.getLottoStatistics();
        if (response.success) {
          setStatistics(response.data);
        }
      } catch (error) {
        console.error('Failed to fetch statistics:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchStatistics();
    const intervalId = setInterval(fetchStatistics, 300000);
    return () => clearInterval(intervalId);
  }, []);

  const formatToKoreanTime = (dateString: string) => {
    const date = new Date(dateString);
    
    return date.toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
      timeZone: 'Asia/Seoul'
    }).replace(/\./g, '.') + ' KST';
  };

  const StatCard = ({ 
    index, 
    title, 
    value, 
    unit 
  }: { 
    index: number;
    title: string;
    value: number | undefined;
    unit: string;
  }) => (
    <motion.div
      whileHover={{ scale: 1.02 }}
      whileTap={{ scale: 0.98 }}
      animate={{
        y: hoveredCard === null ? 0 : hoveredCard === index ? -5 : 0,
        opacity: hoveredCard === null ? 1 : hoveredCard === index ? 1 : 0.7,
      }}
      onHoverStart={() => setHoveredCard(index)}
      onHoverEnd={() => setHoveredCard(null)}
      className="bg-neutral-800 p-4 rounded-xl border border-neutral-700 cursor-pointer transition-shadow hover:shadow-lg"
    >
      <div className="text-sm text-neutral-400 mb-1">{title}</div>
      <div className="text-2xl font-bold text-white">
        {value !== undefined ? value.toLocaleString() : '-'}
        <span className="text-sm text-neutral-400 ml-1">{unit}</span>
      </div>
    </motion.div>
  );

  if (loading || !statistics) return null;

  return (
    <div className="mb-8 relative">
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <StatCard
          index={0}
          title="회차"
          value={statistics.lottoRoundInfo}
          unit="회"
        />
        <StatCard
          index={1}
          title="참여 인원"
          value={statistics.memberCount}
          unit="명"
        />
        <StatCard
          index={2}
          title="발행된 로또"
          value={statistics.lottoPublishCount}
          unit="장"
        />
        <StatCard
          index={3}
          title="총 결제 금액"
          value={statistics.totalPurchaseMoney}
          unit="원"
        />
      </div>
      <motion.div 
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        className="absolute bottom-0 right-0 -mb-6 text-xs text-neutral-500"
      >
        마지막 업데이트: {formatToKoreanTime(statistics.updatedAt)}
      </motion.div>
    </div>
  );
} 