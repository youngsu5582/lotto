import { TicketListResponse, LottoTicketHistory } from '../../types/ticket';
import { mockStore } from './store';

export class TicketMockService {
  async getMyTickets(page: number = 1): Promise<TicketListResponse> {
    const tickets = mockStore.getTickets();
    return {
      tickets,
      totalCount: tickets.length,
    };
  }

  async cancelTicket(ticketId: number): Promise<void> {
    mockStore.updateTicket(ticketId, { status: 'CANCELLED' });
    return Promise.resolve();
  }

  // Mock 데이터 추가를 위한 메서드
  addTicket(ticket: LottoTicketHistory) {
    mockStore.addTicket(ticket);
  }
} 