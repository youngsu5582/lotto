import { BaseApiService } from './base-api';
import { 
  LoginResponse, 
  SignUpResponse,
  LocalLoginRequest, 
  SignUpRequest,
  User
} from '../types/auth';

export class AuthApiService extends BaseApiService {
  private async handleApiError(error: unknown, message: string): Promise<never> {
    console.error('API Error:', error);
    if (error instanceof Error) {
      throw new Error(error.message);
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

  async signUp(data: SignUpRequest): Promise<SignUpResponse> {
    try {
      return await this.fetchJson('/api/auth/register', {
        method: 'POST',
        body: JSON.stringify(data),
      });
    } catch (error) {
      console.error('SignUp error:', error);
      throw error;
    }
  }

  async localLogin(data: LocalLoginRequest): Promise<LoginResponse> {
    try {
      return await this.fetchJson('/api/auth/login', {
        method: 'POST',
        body: JSON.stringify(data),
      });
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  }

  async logout(): Promise<void> {
    try {
      await this.fetchJson('/api/auth/logout', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
        },
      });
    } catch (error) {
      console.error('Logout error:', error);
      throw error;
    }
  }

  async getMe(): Promise<{ data: User }> {
    try {
      const token = localStorage.getItem('accessToken');
      if (!token) {
        throw new Error('No token found');
      }

      return await this.fetchJson('/api/auth', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
    } catch (error) {
      console.error('GetMe error:', error);
      throw error;
    }
  }
}