import { LottoTicketHistory } from '../../types/ticket';

class MockStore {
  private tickets: LottoTicketHistory[] = [];

  addTicket(ticket: LottoTicketHistory) {
    this.tickets.push(ticket);
  }

  getTickets() {
    return this.tickets;
  }

  updateTicket(ticketId: number, updates: Partial<LottoTicketHistory>) {
    const index = this.tickets.findIndex(t => t.id === ticketId);
    if (index >= 0) {
      this.tickets[index] = { ...this.tickets[index], ...updates };
    }
  }
}

export const mockStore = new MockStore(); 