import { screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { renderWithRouter } from '../utils/test-utils';
import Support from './Support';

describe('Support Page', () => {
  it('renders the Support / Admin View heading', () => {
    renderWithRouter(<Support />);
    expect(screen.getByText(/Support/i)).toBeInTheDocument();
  });
});
