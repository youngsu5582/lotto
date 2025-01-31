import { BaseApiService } from './base-api';
import { LottoPurchaseRequest, OrderDataRequest, OrderDataResponse } from '../types/payment';
import { LottoTickets } from '../types/lotto';

export class PaymentApiService extends BaseApiService {
  async verifyPayment(params: { paymentKey: string; orderId: string; amount: number }, tickets: LottoTickets) {
    const requestBody: LottoPurchaseRequest = {
      purchaseHttpRequest: {
        purchaseType: 'CARD',
        currency: 'KRW',
        amount: params.amount,
        paymentKey: params.paymentKey,
        orderId: params.orderId,
      },
      lottoRequest: {
        numbers: tickets,
      },
    };

    return this.fetchJson('/api/tickets', {
      method: 'POST',
      body: JSON.stringify(requestBody),
    });
  }

  async createTemporaryOrder(data: OrderDataRequest): Promise<OrderDataResponse> {
    return this.fetchJson('/api/orders/temporary', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }
} 