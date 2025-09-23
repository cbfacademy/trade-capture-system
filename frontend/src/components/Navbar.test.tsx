import { describe, it, expect } from 'vitest';
import { screen } from '@testing-library/react';
import Navbar from './Navbar';
import React from 'react';
import { renderWithRouter } from '../utils/test-utils';

describe('Navbar Component', () => {
  it('renders the Trading Platform brand', () => {
    renderWithRouter(<Navbar />);
    expect(screen.getByText(/Trading Platform/i)).toBeInTheDocument();
  });
});
