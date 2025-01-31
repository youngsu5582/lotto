import { BaseApiService } from './base-api';
import { LoginResponse, LocalLoginRequest } from '../types/auth';

export class AuthApiService extends BaseApiService {
  async localLogin(data: LocalLoginRequest): Promise<LoginResponse> {
    return this.fetchJson('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async kakaoLogin(code: string): Promise<LoginResponse> {
    return this.fetchJson('/api/auth/kakao', {
      method: 'POST',
      body: JSON.stringify({ code }),
    });
  }

  async logout(): Promise<void> {
    return this.fetchJson('/api/auth/logout', {
      method: 'POST',
    });
  }

  async getMe(): Promise<any> {
    return this.fetchJson('/api/auth/me', {
      method: 'GET',
    });
  }
} 