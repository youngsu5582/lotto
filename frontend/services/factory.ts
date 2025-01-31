import { API_URL } from '../config';
import { AuthApiService } from './auth-api';
import { TicketApiService } from './ticket-api';
import { PaymentApiService } from './payment-api';
import { AuthMockService } from './mock/auth-mock';
import { TicketMockService } from './mock/ticket-mock';
import { PaymentMockService } from './mock/payment-mock';

const isMockAuth = process.env.NEXT_PUBLIC_MOCK_AUTH === 'true';
const isMockPayment = process.env.NEXT_PUBLIC_MOCK_PAYMENT === 'true';
const isMockTicket = process.env.NEXT_PUBLIC_MOCK_TICKET === 'true';

/**
 * Creates service instances based on environment configuration.
 * 
 * @returns An object containing authentication, ticket, and payment services
 * @remarks Dynamically selects between mock and API services based on environment variables
 */
export function createServices() {
  return {
    auth: isMockAuth ? new AuthMockService() : new AuthApiService(API_URL),
    ticket: isMockTicket ? new TicketMockService() : new TicketApiService(API_URL),
    payment: isMockPayment ? new PaymentMockService() : new PaymentApiService(API_URL),
  };
} 