export abstract class BaseApiService {
  protected baseUrl: string;

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  protected async fetchJson(endpoint: string, options: RequestInit = {}) {
    try {
      const response = await fetch(`${this.baseUrl}${endpoint}`, {
        ...options,
        headers: {
          'Content-Type': 'application/json',
          ...options.headers,
        },
      });

      let data;
      try {
        data = await response.json();
      } catch (e) {
        console.error('JSON parse error:', e);
        throw new Error('Invalid JSON response');
      }

      if (!response.ok) {
        if (response.status === 401) {
          localStorage.removeItem('accessToken'); // 인증 실패시 토큰 제거
          throw new Error('인증이 만료되었습니다. 다시 로그인해주세요.');
        }
        const errorMessage = data?.message || response.statusText || 'API call failed';
        console.error('API Error:', {
          status: response.status,
          message: errorMessage,
          data
        });
        throw new Error(errorMessage);
      }

      return data;
    } catch (error) {
      if (error instanceof Error) {
        throw error;
      }
      throw new Error('Unknown API error occurred');
    }
  }
} 