import { screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { renderWithRouter } from '../utils/test-utils';
import TraderSales from './TraderSales';

describe('TraderSales Page', () => {
  it('renders the welcome heading', () => {
    renderWithRouter(<TraderSales />);
    expect(screen.getByText(/Welcome to the Trade Platform/i)).toBeInTheDocument();
  });
});
