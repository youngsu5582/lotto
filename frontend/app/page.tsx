"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import NumberSelector from "../components/NumberSelector";
import Cart from "../components/Cart";
import { LottoTickets } from "../types/lotto";
import { FaShoppingCart, FaTrash } from 'react-icons/fa';
import { motion } from 'framer-motion';

export default function Home() {
  const router = useRouter();
  const [lottoTickets, setLottoTickets] = useState<LottoTickets>([]);
  const [showGuide, setShowGuide] = useState(true);

  const handleAddToCart = (ticket: number[]) => {
    setLottoTickets([...lottoTickets, ticket]);
    // 가이드 숨기기
    setShowGuide(false);
  };

  const handlePurchase = () => {
    if (lottoTickets.length > 0) {
      localStorage.setItem('lottoTickets', JSON.stringify(lottoTickets));
      router.push('/payment');
    }
  };

  const handleClearCart = () => {
    if (window.confirm('장바구니를 비우시겠습니까?')) {
      setLottoTickets([]);
    }
  };

  return (
    <div className="min-h-screen bg-neutral-900 p-8">
      <main className="max-w-4xl mx-auto">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-white mb-4">로또 번호 선택기</h1>
          <p className="text-neutral-400">
            행운의 번호를 선택하고 당신의 꿈을 이루세요!
          </p>
        </div>

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
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-semibold text-white">번호 선택</h2>
              <span className="text-neutral-400 text-sm">
                {lottoTickets.length}/5 티켓
              </span>
            </div>
            <NumberSelector 
              onAddToCart={handleAddToCart}
              cartCount={lottoTickets.length}
            />
          </div>

          <div>
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-semibold text-white flex items-center gap-2">
                <FaShoppingCart className="text-blue-500" />
                장바구니
              </h2>
              {lottoTickets.length > 0 && (
                <button
                  onClick={handleClearCart}
                  className="text-red-500 hover:text-red-400 flex items-center gap-1"
                >
                  <FaTrash size={14} />
                  <span className="text-sm">비우기</span>
                </button>
              )}
            </div>
            <motion.div
              initial={false}
              animate={{ scale: lottoTickets.length > 0 ? 1 : 0.95 }}
              transition={{ duration: 0.2 }}
            >
              <Cart 
                tickets={lottoTickets}
                onPurchase={handlePurchase}
              />
            </motion.div>
          </div>
        </div>

        {lottoTickets.length > 0 && (
          <motion.div 
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="mt-8 text-center text-neutral-400"
          >
            <p>총 {lottoTickets.length}장의 로또를 선택하셨습니다.</p>
            <p className="text-sm">결제 금액: {(lottoTickets.length * 1000).toLocaleString()}원</p>
          </motion.div>
        )}
      </main>
    </div>
  );
}
