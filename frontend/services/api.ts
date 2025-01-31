import { API_URL } from '../config';
import { LottoPurchaseRequest, OrderDataRequest, OrderDataResponse } from '../types/payment';
import { LottoTickets } from '../types/lotto';

class ApiService {
  private baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  private async fetchJson(endpoint: string, options: RequestInit = {}) {
    const response = await fetch(`${this.baseUrl}${endpoint}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    });

    if (!response.ok) {
      throw new Error(`API call failed: ${response.statusText}`);
    }

    return response.json();
  }

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

  // 다른 API 메서드들을 추가할 수 있습니다.
}

export const apiService = new ApiService(API_URL); 