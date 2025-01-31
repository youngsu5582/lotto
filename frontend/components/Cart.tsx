import { LottoTickets } from '../types/lotto';

interface CartProps {
  tickets: LottoTickets;
  onPurchase: () => void;
}

export default function Cart({ tickets, onPurchase }: CartProps) {
  return (
    <div className="bg-neutral-800 p-6 rounded-lg shadow-lg border border-neutral-700">
      <h2 className="text-xl font-semibold mb-4 text-white">장바구니</h2>
      {tickets.map((ticket, idx) => (
        <div key={idx} className="mb-2 p-3 bg-neutral-700 rounded-lg">
          {ticket.map((num) => (
            <span
              key={num}
              className="inline-block w-8 h-8 mx-1 text-center leading-8 bg-blue-500 text-white rounded-full font-semibold"
            >
              {num}
            </span>
          ))}
        </div>
      ))}
      {tickets.length === 0 ? (
        <p className="text-neutral-400 text-center">장바구니가 비어있습니다.</p>
      ) : (
        <button
          onClick={onPurchase}
          className="w-full mt-4 px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
        >
          구매하기
        </button>
      )}
    </div>
  );
} 