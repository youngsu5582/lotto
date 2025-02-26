import { useState } from 'react';
import { authApi } from '../../services';

interface AuthModalProps {
  onClose: () => void;
  onSuccess: () => void;
}

export default function AuthModal({ onClose, onSuccess }: AuthModalProps) {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    // 유효성 검사
    if (!email || !password) {
      setError('이메일과 비밀번호를 모두 입력해주세요.');
      return;
    }

    // 이메일 형식 검사
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setError('올바른 이메일 형식이 아닙니다.');
      return;
    }

    // 비밀번호 길이 검사
    if (password.length < 6) {
      setError('비밀번호는 최소 6자 이상이어야 합니다.');
      return;
    }

    try {
      if (isLogin) {
        // 로그인
        const response = await authApi.localLogin({ email, password });
        if (response.success && response.data?.accessToken) {
          localStorage.setItem('accessToken', response.data.accessToken);
          onSuccess();
        } else {
          setError('로그인에 실패했습니다.');
        }
      } else {
        // 회원가입
        const response = await authApi.signUp({ email, password });
        if (response.success) {
          // 회원가입 성공 후 로그인 모드로 전환
          setIsLogin(true);
          setError('회원가입이 완료되었습니다. 로그인해주세요.');
          // 비밀번호 필드 초기화
          setPassword('');
        } else {
          setError('회원가입에 실패했습니다.');
        }
      }
    } catch (error: any) {
      console.error('Auth error:', error);
      setError(error.message || (isLogin ? '로그인에 실패했습니다.' : '회원가입에 실패했습니다.'));
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-neutral-800 p-6 rounded-lg w-96">
        <h2 className="text-2xl font-bold text-white mb-4">
          {isLogin ? '로그인' : '회원가입'}
        </h2>
        
        <form onSubmit={handleSubmit} className="space-y-4">
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
          {error && (
            <p className={`text-sm ${error.includes('완료되었습니다') ? 'text-green-500' : 'text-red-500'}`}>
              {error}
            </p>
          )}
          <button
            type="submit"
            className="w-full py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            {isLogin ? '로그인' : '회원가입'}
          </button>
        </form>

        <button
          onClick={() => setIsLogin(!isLogin)}
          className="w-full mt-4 text-sm text-blue-400 hover:text-blue-300"
        >
          {isLogin ? '회원가입하기' : '로그인하기'}
        </button>

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