export interface LottoTicketHistory {
  id: number;
  orderId: string;
  numbers: number[][];
  purchaseDate: string;
  amount: number;
  status: 'ACTIVE' | 'CANCELLED';
}

export interface TicketListResponse {
  tickets: LottoTicketHistory[];
  totalCount: number;
} 