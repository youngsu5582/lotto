import { BaseApiService } from './base-api';
import { LottoRoundInfoResponse } from '../types/lotto';

export class LottoApiService extends BaseApiService {
  async getCurrentRoundInfo(): Promise<LottoRoundInfoResponse> {
    return this.fetchJson('/api/lottoes/round-info', {
      method: 'GET',
    });
  }
} 