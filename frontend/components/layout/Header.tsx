"use client";

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { authApi } from '../../services';
import AuthModal from '../auth/AuthModal';
import { User } from '../../types/auth';

interface HeaderProps {
  onAuthStateChange?: (isLoggedIn: boolean) => void;
}

export default function Header({ onAuthStateChange }: HeaderProps) {
  const router = useRouter();
  const [showAuthModal, setShowAuthModal] = useState(false);
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const checkAuth = async () => {
      const token = localStorage.getItem('accessToken');
      if (!token) {
        setUser(null);
        onAuthStateChange?.(false);
        return;
      }

      try {
        const userData = await authApi.getMe();
        setUser(userData.data);
        onAuthStateChange?.(true);
      } catch (error) {
        console.error('Auth check failed:', error);
        localStorage.removeItem('accessToken'); // 토큰이 유효하지 않으면 제거
        setUser(null);
        onAuthStateChange?.(false);
      }
    };

    checkAuth();
  }, []); // onAuthStateChange 제거

  const handleLogout = async () => {
    try {
      await authApi.logout();
      localStorage.removeItem('accessToken');
      setUser(null);
      onAuthStateChange?.(false);
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  return (
    <header className="bg-neutral-800 py-4">
      <div className="max-w-7xl mx-auto px-4 flex justify-between items-center">
        <h1 className="text-white text-xl font-bold cursor-pointer" onClick={() => router.push('/')}>로또</h1>
        <div className="flex items-center gap-4">
          {user ? (
            <>
              <button
                onClick={() => router.push('/my-tickets')}
                className="px-4 py-2 bg-neutral-700 text-white rounded hover:bg-neutral-600"
              >
                구매 내역
              </button>
              <button
                onClick={handleLogout}
                className="px-4 py-2 bg-neutral-700 text-white rounded hover:bg-neutral-600"
              >
                로그아웃
              </button>
            </>
          ) : (
            <button
              onClick={() => setShowAuthModal(true)}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              로그인
            </button>
          )}
        </div>
      </div>

      {showAuthModal && (
        <AuthModal
          onClose={() => setShowAuthModal(false)}
          onSuccess={() => {
            setShowAuthModal(false);
            window.location.reload();
          }}
        />
      )}
    </header>
  );
} 