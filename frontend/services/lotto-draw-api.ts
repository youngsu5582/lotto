import { LottoDrawPreviewNumberResponse } from '@/types/lotto-draw';
import { BaseApiService } from './base-api';

export class LottoDrawApiService extends BaseApiService {
    async getLottoDrawPreview(ticket:number[]): Promise<LottoDrawPreviewNumberResponse> {
      const param = ticket.join(',');
      return this.fetchJson(`/api/draw/statistics/numbers?numbers=${param}`, {
        method: 'GET',
      });
    }
  } 
