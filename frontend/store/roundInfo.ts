import { create } from 'zustand';
import { LottoRoundInfo } from '../types/lotto';
import { lottoApi } from '../services';

interface RoundInfoState {
  roundInfo: LottoRoundInfo | null;
  loading: boolean;
  error: string | null;
  fetchRoundInfo: () => Promise<void>;
  startPolling: (interval?: number) => void;
  stopPolling: () => void;
}

let pollInterval: NodeJS.Timeout | null = null;

export const useRoundInfoStore = create<RoundInfoState>((set) => ({
  roundInfo: null,
  loading: false,
  error: null,

  fetchRoundInfo: async () => {
    set({ loading: true });
    try {
      const response = await lottoApi.getCurrentRoundInfo();
      if (response.success) {
        set({ roundInfo: response.data, error: null });
      }
    } catch (error) {
      console.error('Failed to fetch round info:', error);
      set({ error: '회차 정보를 불러오는데 실패했습니다.' });
    } finally {
      set({ loading: false });
    }
  },

  startPolling: (interval = 300000) => {
    if (pollInterval) return;
    pollInterval = setInterval(async () => {
      const { fetchRoundInfo } = useRoundInfoStore.getState();
      await fetchRoundInfo();
    }, interval);
  },

  stopPolling: () => {
    if (pollInterval) {
      clearInterval(pollInterval);
      pollInterval = null;
    }
  }
})); 