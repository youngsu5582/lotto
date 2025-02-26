export type LottoTicket = number[];
export type LottoTickets = LottoTicket[];

export type LottoStatus = 'ONGOING' | 'COMPLETED' | 'CLOSED';

export interface LottoRoundInfo {
  round: number;
  endDate: string;
  drawDate: string;
  status: LottoStatus;
}

export interface LottoRoundInfoResponse {
  success: boolean;
  status: number;
  message: string;
  data: LottoRoundInfo;
} 