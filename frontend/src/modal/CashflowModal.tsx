import React from "react";
import { Dialog } from "@headlessui/react";
import { CashflowDTO } from "../utils/tradeTypes";
import AGGridTable from "../components/AGGridTable";

interface CashflowModalProps {
  isOpen: boolean;
  onClose: () => void;
  cashflows: CashflowDTO[];
}

const CashflowModal: React.FC<CashflowModalProps> = ({ isOpen, onClose, cashflows }) => {
  // Group cashflows by leg (Pay/Rec + Type)
  const leg1Cashflows = cashflows.filter(
    cf => cf.payRec?.toLowerCase() === 'pay' && cf.paymentType?.toLowerCase() === 'fixed'
  );
  const leg2Cashflows = cashflows.filter(
    cf => cf.payRec?.toLowerCase() === 'receive' && cf.paymentType?.toLowerCase() === 'floating'
  );
  const columnDefs = [
    { headerName: "Value Date", field: "valueDate" },
    { headerName: "Payment Value", field: "paymentValue" },
    { headerName: "Pay/Rec", field: "payRec" },
    { headerName: "Type", field: "paymentType" },
    { headerName: "BDC", field: "paymentBusinessDayConvention" },
    { headerName: "Rate", field: "rate" },
  ];
  return (
    <Dialog open={isOpen} onClose={onClose} className="fixed inset-0 z-50 flex items-start justify-center">
      {/* Animated background overlay */}
      <div
        aria-hidden="true"
        className={`fixed inset-0 bg-black/30 transition-opacity duration-300 ${isOpen ? 'opacity-100' : 'opacity-0'}`}
      />
      <div className="relative bg-white rounded-lg shadow-lg mt-10 p-6 transition-transform duration-400 ease-in-out transform animate-slide-down-tw" style={{ width: '1200px', maxWidth: '98vw' }}>
        <Dialog.Title className="text-xl font-bold mb-4">Generated Cashflows</Dialog.Title>
        <div className="flex flex-row gap-8">
          <div className="flex-1">
            <div className="font-semibold text-center mb-2">Leg 1 Cashflows</div>
            <AGGridTable columnDefs={columnDefs} rowData={leg1Cashflows} rowSelection="single" />
          </div>
          <div className="flex-1">
            <div className="font-semibold text-center mb-2">Leg 2 Cashflows</div>
            <AGGridTable columnDefs={columnDefs} rowData={leg2Cashflows} rowSelection="single" />
          </div>
        </div>
        <div className="flex justify-end mt-4">
          <button onClick={onClose} className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-800">Close</button>
        </div>
      </div>
      <style>{`
        .animate-slide-down-tw {
          @apply translate-y-[-100px] opacity-0;
          animation: slideDownTW 0.4s cubic-bezier(0.4, 0, 0.2, 1) forwards;
        }
        @keyframes slideDownTW {
          0% { transform: translateY(-100px); opacity: 0; }
          100% { transform: translateY(0); opacity: 1; }
        }
      `}</style>
    </Dialog>
  );
};

export default CashflowModal;
