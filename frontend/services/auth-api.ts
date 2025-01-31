import { BaseApiService } from './base-api';
import { LoginResponse, LocalLoginRequest } from '../types/auth';

export class AuthApiService extends BaseApiService {
  private async handleApiError(error: unknown, message: string): Promise<never> {
    if (error instanceof Error) {
      throw new Error(`${message}: ${error.message}`);
    }
    throw new Error(`${message}: 알 수 없는 오류가 발생했습니다`);
  }

  private async withErrorHandling<T>(
    apiCall: () => Promise<T>,
    errorMessage: string
  ): Promise<T> {
    try {
      return await apiCall();
    } catch (error: unknown) {
      return this.handleApiError(error, errorMessage);
    }
  }

  async localLogin(data: LocalLoginRequest): Promise<LoginResponse> {
    return this.withErrorHandling(
      () => this.fetchJson('/api/auth/login', {
        method: 'POST',
        body: JSON.stringify(data),
      }),
      '로그인 실패'
    );
  }

  async kakaoLogin(code: string): Promise<LoginResponse> {
    return this.withErrorHandling(
      () => this.fetchJson('/api/auth/kakao', {
        method: 'POST',
        body: JSON.stringify({ code }),
      }),
      '카카오 로그인 실패'
    );
  }

  async logout(): Promise<void> {
    return this.withErrorHandling(
      () => this.fetchJson('/api/auth/logout', {
        method: 'POST',
      }),
      '로그아웃 실패'
    );
  }

  async getMe(): Promise<any> {
    return this.withErrorHandling(
      () => this.fetchJson('/api/auth/me', {
        method: 'GET',
      }),
      '사용자 정보 조회 실패'
    );
  }
}