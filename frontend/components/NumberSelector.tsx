import { useState } from 'react';
import { LottoTicket } from '../types/lotto';
import { motion } from 'framer-motion';
import { FaRandom } from 'react-icons/fa';

interface NumberSelectorProps {
  onAddToCart: (ticket: LottoTicket) => void;
  cartCount: number;
}

export default function NumberSelector({ onAddToCart, cartCount }: NumberSelectorProps) {
  const [currentNumbers, setCurrentNumbers] = useState<number[]>([]);
  const numbers = Array.from({length: 45}, (_, i) => i + 1);

  const handleNumberSelect = (num: number) => {
    if (currentNumbers.includes(num)) {
      setCurrentNumbers(currentNumbers.filter(n => n !== num));
    } else if (currentNumbers.length < 6) {
      setCurrentNumbers([...currentNumbers, num].sort((a, b) => a - b));
    }
  };

  const handleAddToCart = () => {
    if (currentNumbers.length === 6 && cartCount < 5) {
      onAddToCart(currentNumbers);
      setCurrentNumbers([]);
    }
  };

  const handleAutoSelect = () => {
    if (cartCount >= 5) return;
    
    const autoNumbers: number[] = [];
    while (autoNumbers.length < 6) {
      const num = Math.floor(Math.random() * 45) + 1;
      if (!autoNumbers.includes(num)) {
        autoNumbers.push(num);
      }
    }
    setCurrentNumbers(autoNumbers.sort((a, b) => a - b));
  };

  const getNumberColor = (num: number) => {
    if (num <= 10) return 'bg-yellow-500 hover:bg-yellow-600';
    if (num <= 20) return 'bg-blue-500 hover:bg-blue-600';
    if (num <= 30) return 'bg-red-500 hover:bg-red-600';
    if (num <= 40) return 'bg-green-500 hover:bg-green-600';
    return 'bg-purple-500 hover:bg-purple-600';
  };

  return (
    <div className="bg-neutral-800 p-6 rounded-lg shadow-lg border border-neutral-700">
      {/* 선택된 번호 표시 영역 */}
      <div className="mb-6 p-4 bg-neutral-700 rounded-lg">
        <div className="flex items-center justify-between mb-3">
          <span className="text-white text-lg">선택된 번호</span>
          <button
            onClick={handleAutoSelect}
            disabled={cartCount >= 5}
            className="flex items-center gap-2 px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50 disabled:hover:bg-blue-600"
          >
            <FaRandom />
            <span className="text-sm">자동 선택</span>
          </button>
        </div>
        <div className="flex gap-2 h-12">
          {[...Array(6)].map((_, idx) => (
            <div
              key={idx}
              className={`w-12 h-12 rounded-full flex items-center justify-center text-lg font-bold
                ${currentNumbers[idx] 
                  ? getNumberColor(currentNumbers[idx])
                  : 'bg-neutral-600 border-2 border-dashed border-neutral-500'
                }`}
            >
              {currentNumbers[idx] || ''}
            </div>
          ))}
        </div>
      </div>

      {/* 번호 선택 그리드 */}
      <div className="grid grid-cols-7 sm:grid-cols-9 gap-2 mb-6">
        {numbers.map((num) => (
          <motion.button
            key={num}
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => handleNumberSelect(num)}
            className={`w-10 h-10 rounded-full font-semibold text-white transition-colors
              ${currentNumbers.includes(num)
                ? 'ring-2 ring-white ring-offset-2 ring-offset-neutral-800 ' + getNumberColor(num)
                : 'bg-neutral-700 hover:bg-neutral-600'
              }`}
          >
            {num}
          </motion.button>
        ))}
      </div>

      {/* 장바구니 추가 버튼 */}
      <button
        onClick={handleAddToCart}
        disabled={currentNumbers.length !== 6 || cartCount >= 5}
        className="w-full px-6 py-3 bg-blue-600 text-white rounded-lg disabled:bg-neutral-700 disabled:text-neutral-400 hover:bg-blue-700 transition-colors"
      >
        {currentNumbers.length === 6 
          ? '장바구니에 추가하기' 
          : `${6 - currentNumbers.length}개 더 선택하세요`
        }
      </button>
    </div>
  );
} 