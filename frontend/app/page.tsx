"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import NumberSelector from '../components/NumberSelector';
import Cart from '../components/Cart';
import { LottoTickets } from '../types/lotto';
import { FaShoppingCart } from 'react-icons/fa';
import { motion } from 'framer-motion';
import Header from '../components/layout/Header';

export default function Home() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [tickets, setTickets] = useState<LottoTickets>([]);
  const router = useRouter();

  const handleAddToCart = (ticket: number[]) => {
    setTickets([...tickets, ticket]);
  };

  const handlePurchase = () => {
    if (!isLoggedIn) {
      alert('로그인이 필요합니다.');
      return;
    }
    localStorage.setItem('lottoTickets', JSON.stringify(tickets));
    router.push('/payment');
  };

  const handleAuthStateChange = (loggedIn: boolean) => {
    setIsLoggedIn(loggedIn);
  };

  return (
    <div className="min-h-screen bg-neutral-900 p-8">
      <Header onAuthStateChange={handleAuthStateChange} />
      
      <main className="max-w-4xl mx-auto">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-white mb-4">로또 번호 선택기</h1>
          <p className="text-neutral-400">
            행운의 번호를 선택하고 당신의 꿈을 이루세요!
          </p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div className="lg:col-span-2">
            <NumberSelector 
              onAddToCart={handleAddToCart} 
              cartCount={tickets.length} 
            />
          </div>
          <div>
            <Cart 
              tickets={tickets} 
              onPurchase={handlePurchase}
            />
          </div>
        </div>
      </main>
    </div>
  );
}
