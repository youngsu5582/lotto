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
  lottoRoundInfoId: number;
  memberCount: number;
  lottoPublishCount: number;
  totalPurchaseMoney: number;
}

export interface LottoStatisticsResponse {
  success: boolean;
  status: number;
  message: string;
  data: LottoStatistics;
} 