import { API_URL } from '../config';
import { AuthApiService } from './auth-api';
import { TicketApiService } from './ticket-api';
import { PaymentApiService } from './payment-api';
import { LottoApiService } from './lotto-api';

interface Services {
    auth: AuthApiService;
    ticket: TicketApiService;
    payment: PaymentApiService;
    lotto: LottoApiService;
}

const isMockAuth = process.env.NEXT_PUBLIC_MOCK_AUTH === 'true' || false;
const isMockPayment = process.env.NEXT_PUBLIC_MOCK_PAYMENT === 'true' || false;
const isMockTicket = process.env.NEXT_PUBLIC_MOCK_TICKET === 'true' || false;

export function createServices() : Services {
  return {
    auth: new AuthApiService(API_URL),
    ticket: new TicketApiService(API_URL),
    payment: new PaymentApiService(API_URL),
    lotto: new LottoApiService(API_URL),
  };
} 
