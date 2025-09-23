import React from 'react';

interface SnackbarProps {
  open: boolean;
  message: string;
  type?: 'success' | 'error';
  onClose: () => void;
}

const typeStyles = {
  success: 'bg-emerald-500 text-white',
  error: 'bg-rose-500 text-white',
};

const Snackbar: React.FC<SnackbarProps> = ({ open, message, type = 'success', onClose }) => {
  if (!open) return null;
  return (
    <div className="fixed top-6 left-1/2 transform -translate-x-1/2 z-50 flex justify-center w-full pointer-events-none">
      <div
        className={`pointer-events-auto px-6 py-3 rounded-xl shadow-lg font-semibold text-center transition-all duration-300 ${typeStyles[type]}`}
        role="alert"
        onClick={onClose}
      >
        {message}
      </div>
    </div>
  );
};

export default Snackbar;

