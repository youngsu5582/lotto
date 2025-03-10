import { createServices } from './factory';

const services = createServices();

export const authApi = services.auth;
export const ticketApi = services.ticket;
export const paymentApi = services.payment;
export const lottoApi = services.lotto; 
export const lottoDrawApi = services.lottoDraw;