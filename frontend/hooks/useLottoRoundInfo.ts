import { useEffect } from 'react';
import { useRoundInfoStore } from '../store/roundInfo';

export function useLottoRoundInfo(refreshInterval = 300000) {
  const { roundInfo, loading, error, fetchRoundInfo, startPolling, stopPolling } = useRoundInfoStore();

  useEffect(() => {
    fetchRoundInfo();
    startPolling(refreshInterval);
    return () => stopPolling();
  }, [refreshInterval]);

  return { roundInfo, loading, error, refetch: fetchRoundInfo };
} 