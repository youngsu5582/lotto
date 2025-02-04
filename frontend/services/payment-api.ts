import { BaseApiService } from './base-api';
import { LottoPurchaseRequest, OrderDataRequest, OrderDataResponse } from '../types/payment';
import { LottoTickets } from '../types/lotto';

export class PaymentApiService extends BaseApiService {
  private readonly TICKET_PRICE = 1000; // 티켓당 1,000원

  async verifyPayment(
    params: { 
      paymentKey: string; 
      orderId: string; 
      amount: number;
      lottoPublishId: number;
      purchaseType?: 'CARD';
      currency?: 'KRW';
    }
  ) {
    // 결제 금액 검증
    if (params.amount <= 0) {
      throw new Error('결제 금액은 0보다 커야 합니다.');
    }

    const requestBody: LottoPurchaseRequest = {
      purchaseHttpRequest: {
        purchaseType: params.purchaseType || 'CARD',
        currency: params.currency || 'KRW',
        amount: params.amount,
        paymentKey: params.paymentKey,
        orderId: params.orderId,
      },
      lottoPublishId: params.lottoPublishId
    };

    return this.fetchJson('/api/tickets', {
      method: 'POST',
      body: JSON.stringify(requestBody),
    });
  }

  async createTemporaryOrder(data: OrderDataRequest): Promise<OrderDataResponse> {
    return this.fetchJson('/api/orders', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }
}