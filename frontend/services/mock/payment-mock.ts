import { OrderDataRequest, OrderDataResponse } from '../../types/payment';
import { mockStore } from './store';

export class PaymentMockService {
  async createTemporaryOrder(data: OrderDataRequest): Promise<OrderDataResponse> {
    return {
      success: true,
      message: 'Mock temporary order created',
    };
  }

  async verifyPayment(params: any, tickets: number[][]) {
    const mockTicket = {
      id: Date.now(),
      orderId: params.orderId,
      numbers: tickets,
      purchaseDate: new Date().toISOString(),
      amount: tickets.length * 1000,
      status: 'ACTIVE' as const,
    };
    
    mockStore.addTicket(mockTicket);
    return { success: true };
  }
} 