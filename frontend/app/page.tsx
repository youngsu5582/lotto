"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import NumberSelector from "../components/NumberSelector";
import Cart from "../components/Cart";
import { LottoTickets } from "../types/lotto";
import { FaShoppingCart } from 'react-icons/fa';
import { motion } from 'framer-motion';
import Header from '../components/layout/Header';

export default function Home() {
  const router = useRouter();
  const [lottoTickets, setLottoTickets] = useState<LottoTickets>([]);
  const [showGuide, setShowGuide] = useState(true);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleAuthStateChange = (loggedIn: boolean) => {
    setIsLoggedIn(loggedIn);
  };

  const handleAddToCart = (ticket: number[]) => {
    setLottoTickets([...lottoTickets, ticket]);
    setShowGuide(false);
  };

  const handlePurchase = () => {
    if (lottoTickets.length > 0) {
      localStorage.setItem('lottoTickets', JSON.stringify(lottoTickets));
      router.push('/payment');
    }
  };

  return (
    <div className="min-h-screen bg-neutral-900">
      <Header onAuthStateChange={handleAuthStateChange} />
      
      <main className="max-w-4xl mx-auto p-8">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-white mb-4">로또 번호 선택기</h1>
          <p className="text-neutral-400">
            행운의 번호를 선택하고 당신의 꿈을 이루세요!
          </p>
        </div>

        {!isLoggedIn ? (
          <div className="text-center">
            <p className="text-white mb-4">로또 구매를 위해 로그인해주세요.</p>
          </div>
        ) : (
          <>
            {showGuide && lottoTickets.length === 0 && (
              <motion.div 
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                className="bg-blue-600 bg-opacity-20 p-6 rounded-lg mb-8 text-center"
              >
                <p className="text-blue-300 mb-2">✨ 로또 구매 가이드</p>
                <ul className="text-blue-100 text-sm space-y-2">
                  <li>• 1부터 45까지의 숫자 중 6개를 선택하세요</li>
                  <li>• 최대 5개의 티켓을 구매할 수 있습니다</li>
                  <li>• 각 티켓의 가격은 1,000원입니다</li>
                </ul>
              </motion.div>
            )}

            <div className="grid md:grid-cols-2 gap-8">
              <div>
                <NumberSelector 
                  onAddToCart={handleAddToCart}
                  cartCount={lottoTickets.length}
                />
              </div>
              <div>
                <Cart 
                  tickets={lottoTickets}
                  onPurchase={handlePurchase}
                />
              </div>
            </div>
          </>
        )}
      </main>
    </div>
  );
}
