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
      <div className="flex justify-between items-center mb-4">
        <div className="text-white">
          <span className="text-lg">{currentNumbers.length}</span>
          <span className="text-neutral-400 text-sm">/6 선택됨</span>
        </div>
        <button
          onClick={handleAutoSelect}
          disabled={cartCount >= 5}
          className="flex items-center gap-2 px-3 py-1 bg-neutral-700 text-white rounded hover:bg-neutral-600 disabled:opacity-50"
        >
          <FaRandom />
          <span className="text-sm">자동 선택</span>
        </button>
      </div>

      <motion.div 
        className="grid grid-cols-9 gap-2"
        initial={false}
        animate={{ scale: currentNumbers.length === 6 ? 1.02 : 1 }}
        transition={{ duration: 0.2 }}
      >
        {numbers.map((num) => (
          <motion.button
            key={num}
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => handleNumberSelect(num)}
            aria-label={`숫자 ${num} ${currentNumbers.includes(num) ? '선택됨' : ''}`}
            aria-pressed={currentNumbers.includes(num)}
            className={`w-10 h-10 rounded-full font-semibold text-white transition-colors
              ${currentNumbers.includes(num)
                ? 'ring-2 ring-white ring-offset-2 ring-offset-neutral-800 ' + getNumberColor(num)
                : 'bg-neutral-700 hover:bg-neutral-600'
              }`}
          >
            {num}
          </motion.button>
        ))}
      </motion.div>

      <div className="mt-6">
        <button
          onClick={handleAddToCart}
          disabled={currentNumbers.length !== 6 || cartCount >= 5}
          className="w-full px-6 py-3 bg-blue-600 text-white rounded-lg disabled:bg-neutral-700 hover:bg-blue-700 transition-colors relative overflow-hidden"
        >
          {currentNumbers.length === 6 ? (
            <motion.span
              initial={{ y: 20 }}
              animate={{ y: 0 }}
            >
              장바구니에 추가하기
            </motion.span>
          ) : (
            `${6 - currentNumbers.length}개 더 선택하세요`
          )}
        </button>
      </div>
    </div>
  );
} 