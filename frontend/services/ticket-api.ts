import { BaseApiService } from './base-api';
import { BillsResponse } from '../types/ticket';

export class TicketApiService extends BaseApiService {
  async getMyTickets(): Promise<BillsResponse> {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      throw new Error('인증 토큰이 없습니다.');
    }

    return this.fetchJson('/api/lottoes', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
  }

  async cancelTicket(billId: number): Promise<void> {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      throw new Error('인증 토큰이 없습니다.');
    }

    return this.fetchJson(`/api/cancel`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify({ billId })
    });
  }
} 