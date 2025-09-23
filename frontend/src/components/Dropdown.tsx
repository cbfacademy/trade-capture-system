import React from "react";

interface DropdownOption {
  value: string | number;
  label: string;
}

interface DropdownProps {
  id?: string;
  name?: string;
  options: DropdownOption[];
  value: string | number;
  onChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
  className?: string;
  disabled?: boolean;
  placeholder?: string;
}

const Dropdown: React.FC<DropdownProps> = ({
  id,
  name,
  options,
  value,
  onChange,
  className = "",
  disabled = false,
  placeholder = "Select...",
}) => (
  <select
    id={id}
    name={name}
    value={value}
    onChange={onChange}
    className={`block  h-7 min-w-[185px] px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 text-sm ${className}`}
    disabled={disabled}
  >
    <option value="" disabled>{placeholder}</option>
    {options.map((option) => (
      <option key={option.label} value={option.value}>
        {option.label}
      </option>
    ))}
  </select>
);

export default Dropdown;

