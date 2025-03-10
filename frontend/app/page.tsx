"use client";

import { useState, useEffect } from "react";
import { useRouter } from 'next/navigation';
import NumberSelector from '../components/NumberSelector';
import Cart from '../components/Cart';
import { LottoTickets } from '../types/lotto';
import { FaShoppingCart } from 'react-icons/fa';
import { motion } from 'framer-motion';
import Header from '../components/layout/Header';
import PageHeader from '../components/layout/PageHeader';
import LottoStatistics from '../components/LottoStatistics';

export default function Home() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [tickets, setTickets] = useState<LottoTickets>([]);
  const router = useRouter();
  const [selectedNumbers, setSelectedNumbers] = useState<number[]>([]);

  const handleAddToCart = (ticket: number[]) => {
    if (tickets.length >= 5) {
      alert('최대 5개까지만 구매할 수 있습니다.');
      return;
    }
    setTickets([...tickets, ticket]);
  };

  const handleRemoveTicket = (index: number) => {
    setTickets(tickets.filter((_, i) => i !== index));
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

  const handleRemoveNumber = (index: number) => {
    setSelectedNumbers(prev => prev.filter((_, i) => i !== index));
  };

  return (
    <div className="min-h-screen bg-neutral-900">
      <Header onAuthStateChange={handleAuthStateChange} />
      
      <PageHeader
        title="로또 번호 선택기"
        subtitle="행운의 번호를 선택하고 당신의 꿈을 이루세요!"
        rightContent="최대 5개까지 구매 가능"
      />
      
      <main className="max-w-4xl mx-auto px-4 pb-8">
        <div className="bg-neutral-800/50 rounded-2xl p-6 shadow-lg border border-neutral-700">
          <LottoStatistics />
          
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <div className="lg:col-span-2">

              <NumberSelector 
                onAddToCart={handleAddToCart} 
                cartCount={tickets.length} 
              />
            </div>
            <div className="lg:sticky lg:top-4">
              <Cart 
                tickets={tickets}
                onRemoveTicket={handleRemoveTicket}
                onPurchase={handlePurchase}
              />
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
