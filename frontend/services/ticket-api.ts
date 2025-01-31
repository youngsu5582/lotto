import { BaseApiService } from './base-api';
import { TicketListResponse } from '../types/ticket';

export class TicketApiService extends BaseApiService {
  async getMyTickets(page: number = 1): Promise<TicketListResponse> {
    return this.fetchJson(`/api/tickets/my?page=${page}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
      },
    });
  }

  async cancelTicket(ticketId: number): Promise<void> {
    return this.fetchJson(`/api/tickets/${ticketId}/cancel`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
      },
    });
  }
} 