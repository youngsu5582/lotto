import { LoginResponse, LocalLoginRequest, User } from '../../types/auth';

const MOCK_USER: User = {
  id: 1,
  email: 'test@example.com',
  name: '테스트 사용자',
};

export class AuthMockService {
  async localLogin(data: LocalLoginRequest): Promise<LoginResponse> {
    // 간단한 검증
    if (data.email === 'test@example.com' && data.password === 'password') {
      return {
        accessToken: 'mock_token',
        user: MOCK_USER,
      };
    }
    throw new Error('Invalid credentials');
  }

  async kakaoLogin(code: string): Promise<LoginResponse> {
    return {
      accessToken: 'mock_token',
      user: MOCK_USER,
    };
  }

  async logout(): Promise<void> {
    return Promise.resolve();
  }

  async getMe(): Promise<User> {
    const token = localStorage.getItem('accessToken');
    if (!token) throw new Error('No token');
    return MOCK_USER;
  }
} 