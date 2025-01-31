import { useState } from 'react';
import { authApi } from '../../services';

interface LoginModalProps {
  onClose: () => void;
  onSuccess: () => void;
}

export default function LoginModal({ onClose, onSuccess }: LoginModalProps) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLocalLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await authApi.localLogin({ email, password });
      localStorage.setItem('accessToken', response.accessToken);
      onSuccess();
    } catch (error) {
      setError('로그인에 실패했습니다.');
    }
  };

  const handleKakaoLogin = () => {
    const KAKAO_CLIENT_ID = process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID;
    const REDIRECT_URI = `${window.location.origin}/auth/kakao/callback`;
    const kakaoAuthUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${KAKAO_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;
    window.location.href = kakaoAuthUrl;
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-neutral-800 p-6 rounded-lg w-96">
        <h2 className="text-2xl font-bold text-white mb-4">로그인</h2>
        
        <form onSubmit={handleLocalLogin} className="space-y-4">
          <div>
            <input
              type="email"
              placeholder="이메일"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full p-2 rounded bg-neutral-700 text-white"
            />
          </div>
          <div>
            <input
              type="password"
              placeholder="비밀번호"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-2 rounded bg-neutral-700 text-white"
            />
          </div>
          {error && <p className="text-red-500">{error}</p>}
          <button
            type="submit"
            className="w-full py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            로그인
          </button>
        </form>

        <div className="mt-4">
          <button
            onClick={handleKakaoLogin}
            className="w-full py-2 bg-yellow-500 text-black rounded hover:bg-yellow-600"
          >
            카카오로 로그인
          </button>
        </div>

        <button
          onClick={onClose}
          className="mt-4 w-full py-2 bg-neutral-700 text-white rounded hover:bg-neutral-600"
        >
          닫기
        </button>
      </div>
    </div>
  );
} 