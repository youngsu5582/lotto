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

export interface LottoStatistics {
  lottoRoundInfo: number;
  memberCount: number;
  lottoPublishCount: number;
  totalPurchaseMoney: number;
  updatedAt: string;
}

export interface LottoStatisticsResponse {
  success: boolean;
  status: number;
  message: string;
  data: LottoStatistics;
} 