import { API_URL } from '../config';
import { AuthApiService } from './auth-api';
import { TicketApiService } from './ticket-api';
import { PaymentApiService } from './payment-api';
import { AuthMockService } from './mock/auth-mock';
import { TicketMockService } from './mock/ticket-mock';
import { PaymentMockService } from './mock/payment-mock';

interface Services {
    auth: AuthApiService | AuthMockService;
    ticket: TicketApiService | TicketMockService;
    payment: PaymentApiService | PaymentMockService;
}

const isMockAuth = process.env.NEXT_PUBLIC_MOCK_AUTH === 'true';
const isMockPayment = process.env.NEXT_PUBLIC_MOCK_PAYMENT === 'true';
const isMockTicket = process.env.NEXT_PUBLIC_MOCK_TICKET === 'true';

export function createServices() : Services {
  return {
    auth: isMockAuth ? new AuthMockService() : new AuthApiService(API_URL),
    ticket: isMockTicket ? new TicketMockService() : new TicketApiService(API_URL),
    payment: isMockPayment ? new PaymentMockService() : new PaymentApiService(API_URL),
  };
} 