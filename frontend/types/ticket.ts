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

export interface LottoRoundInfo {
  round: number;
  startDate: string;
  endDate: string;
  drawDate: string;
  paymentDeadline: string;
  status: string;
}

export interface LottoPublish {
  status: string;
  lottoRoundInfo: LottoRoundInfo;
  lottoes: number[][];
  issuedAt: string;
}

export interface Purchase {
  id: string;
  amount: number;
}

export interface Bill {
  billId: number;
  paymentResponse: Purchase;
  publishResponse: LottoPublish;
}

export interface BillsResponse {
  success: boolean;
  status: number;
  message: string;
  data: {
    response: Bill[];
  };
} 