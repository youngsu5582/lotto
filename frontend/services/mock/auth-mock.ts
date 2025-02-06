import { LoginResponse, LocalLoginRequest, User, SignUpRequest } from '../../types/auth';

const MOCK_USER: User = {
  id: 1,
  email: 'test@example.com',
};

export class AuthMockService {
  async localLogin(data: LocalLoginRequest): Promise<LoginResponse> {
    // 테스트용 계정
    if (data.email === 'test@example.com' && data.password === 'password') {
      return {
        accessToken: 'mock_token',
        user: MOCK_USER,
      };
    }
    throw new Error('이메일 또는 비밀번호가 올바르지 않습니다.');
  }

  async signUp(data: SignUpRequest): Promise<LoginResponse> {
    // 이메일 중복 체크 시뮬레이션
    if (data.email === 'test@example.com') {
      throw new Error('이미 사용 중인 이메일입니다.');
    }
    return {
      accessToken: 'mock_token',
      user: {
        ...MOCK_USER,
        email: data.email,
      },
    };
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