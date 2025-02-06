export interface ApiResponse<T> {
  success: boolean;
  status: number;
  message: string;
  data: T;
}

export interface AuthToken {
  accessToken: string;
}

export interface LoginResponse extends ApiResponse<AuthToken> {}

export interface SignUpResponse extends ApiResponse<void> {}

export interface LocalLoginRequest {
  email: string;
  password: string;
}

export interface SignUpRequest {
  email: string;
  password: string;
}

export interface User {
  id: number;
  email: string;
} 