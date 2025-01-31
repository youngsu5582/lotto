export interface LottoPurchaseRequest {
  purchaseHttpRequest: {
    purchaseType: 'CARD' | 'TOSS_PAY';
    currency: 'KRW';
    amount: number;
    paymentKey: string;
    orderId: string;
  };
  lottoRequest: {
    numbers: number[][];
  };
}

export interface OrderDataRequest {
  orderId: string;
  amount: number;
  numbers: number[][];
}

export interface OrderDataResponse {
  success: boolean;
  message?: string;
} 