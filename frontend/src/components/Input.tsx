import React from 'react';

interface InputProps extends Omit<React.InputHTMLAttributes<HTMLInputElement>, 'size'> {
  variant?: 'primary' | 'secondary';
  size?: 'sm' | 'md' | 'lg';
  label?: string;
  error?: string;
}

const sizeClasses = {
  sm: 'px-2 py-1 text-sm',
  md: 'px-3 py-2 text-base',
  lg: 'px-4 py-3 text-lg',
};

const variantClasses = {
  primary: 'border-gray-400 focus:border-gray-600 focus:ring-gray-200',
  secondary: 'border-red-600 focus:border-red-700 focus:ring-red-200',
};

const Input: React.FC<InputProps> = ({
  variant = 'primary',
  size = 'md',
  label,
  error,
  className = '',
  ...props
}) => (
  <div className="flex flex-col gap-1">
    <input
      placeholder={props.placeholder || label}
      className={`border rounded outline-none focus:ring-2 min-w-[185px] shadow-2xl text-black transition ${variantClasses[variant]} ${sizeClasses[size]} ${className}`}
      {...props}
    />
    {error && <span className="text-red-600 text-xs mt-1">{error}</span>}
  </div>
);

export default Input;

