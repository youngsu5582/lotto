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
      purchaseType?: 'CARD' | 'TOSS_PAY';
      currency?: 'KRW';
    }, 
    tickets: LottoTickets
  ) {
    // 결제 금액 검증
    if (params.amount <= 0) {
      throw new Error('결제 금액은 0보다 커야 합니다.');
    }

    const expectedAmount = tickets.length * this.TICKET_PRICE;
    if (params.amount !== expectedAmount) {
      throw new Error(`결제 금액이 올바르지 않습니다. 예상 금액: ${expectedAmount}원, 실제 금액: ${params.amount}원`);
    }

    const requestBody: LottoPurchaseRequest = {
      purchaseHttpRequest: {
        purchaseType: params.purchaseType || 'CARD',
        currency: params.currency || 'KRW',
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