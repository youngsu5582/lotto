import { BaseApiService } from './base-api';
import { LottoRoundInfoResponse, LottoStatisticsResponse } from '../types/lotto';

export class LottoApiService extends BaseApiService {
  async getCurrentRoundInfo(): Promise<LottoRoundInfoResponse> {
    return this.fetchJson('/api/lottoes/round-info', {
      method: 'GET',
    });
  }

  async getLottoStatistics(): Promise<LottoStatisticsResponse> {
    return this.fetchJson('/api/lottoes/statistics', {
      method: 'GET',
    });
  }
} 