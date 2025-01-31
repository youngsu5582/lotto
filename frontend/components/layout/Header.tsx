"use client";

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { authApi } from '../../services';
import LoginModal from '../auth/LoginModal';
import { User } from '../../types/auth';

export default function Header() {
  const router = useRouter();
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const userData = await authApi.getMe();
        setUser(userData);
      } catch (error) {
        setUser(null);
      }
    };

    checkAuth();
  }, []);

  const handleLogout = async () => {
    try {
      await authApi.logout();
      localStorage.removeItem('accessToken');
      setUser(null);
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  return (
    <header className="bg-neutral-800 py-4">
      <div className="max-w-7xl mx-auto px-4 flex justify-between items-center">
        <h1 className="text-white text-xl font-bold">로또</h1>
        <div className="flex items-center gap-4">
          {user ? (
            <>
              <button
                onClick={() => router.push('/my-tickets')}
                className="px-4 py-2 bg-neutral-700 text-white rounded hover:bg-neutral-600"
              >
                구매 내역
              </button>
              <div className="flex items-center gap-4">
                <span className="text-white">{user.name}님</span>
                <button
                  onClick={handleLogout}
                  className="px-4 py-2 bg-neutral-700 text-white rounded hover:bg-neutral-600"
                >
                  로그아웃
                </button>
              </div>
            </>
          ) : (
            <button
              onClick={() => setShowLoginModal(true)}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              로그인
            </button>
          )}
        </div>
      </div>

      {showLoginModal && (
        <LoginModal
          onClose={() => setShowLoginModal(false)}
          onSuccess={() => {
            setShowLoginModal(false);
            window.location.reload();
          }}
        />
      )}
    </header>
  );
} 