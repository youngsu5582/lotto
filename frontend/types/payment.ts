export interface LottoPurchaseRequest {
  purchaseHttpRequest: {
    purchaseType: 'CARD';
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