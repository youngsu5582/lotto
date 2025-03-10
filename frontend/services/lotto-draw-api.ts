import { LottoDrawPreviewNumberResponse } from '@/types/lotto-draw';
import { BaseApiService } from './base-api';

export class LottoDrawApiServce extends BaseApiService {
    async getLottoDrawPrivew(ticket:number[]): Promise<LottoDrawPreviewNumberResponse> {
      const param = ticket.join(',');
      return this.fetchJson(`/api/draw/statistics/numbers?numbers=${param}`, {
        method: 'GET',
      });
    }
  } 