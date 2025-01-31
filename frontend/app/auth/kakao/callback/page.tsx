"use client";

import { useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { authApi } from '../../../../services';

export default function KakaoCallback() {
  const router = useRouter();
  const searchParams = useSearchParams();

  useEffect(() => {
    const handleKakaoCallback = async () => {
      const code = searchParams.get('code');
      if (!code) {
        router.push('/');
        return;
      }

      try {
        const response = await authApi.kakaoLogin(code);
        localStorage.setItem('accessToken', response.accessToken);
        router.push('/');
      } catch (error) {
        console.error('Kakao login failed:', error);
        router.push('/');
      }
    };

    handleKakaoCallback();
  }, [router, searchParams]);

  return (
    <div className="min-h-screen bg-neutral-900 flex items-center justify-center">
      <div className="text-white">카카오 로그인 처리 중...</div>
    </div>
  );
} 