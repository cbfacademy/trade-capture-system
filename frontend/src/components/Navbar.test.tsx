import { screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { renderWithRouter } from '../utils/test-utils';
import Navbar from './Navbar';

describe('Navbar Component', () => {
  it('renders the Trading Platform brand', () => {
    renderWithRouter(<Navbar />);
    expect(screen.getByText(/Trading Platform/i)).toBeInTheDocument();
  });
});
