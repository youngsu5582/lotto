export interface LottoPurchaseRequest {
  purchaseHttpRequest: {
    purchaseType: 'CARD';
    currency: 'KRW';
    amount: number;
    paymentKey: string;
    orderId: string;
  };
  lottoPublishId: number;
}

export interface OrderDataRequest {
  numbers: number[][];
}

export interface OrderDataResponse {
  success: boolean;
  status: number;
  message: string;
  data: {
    lottoPublishId: number;
    orderId: string;
  };
} 