import { screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { renderWithRouter } from '../utils/test-utils';
import MiddleOffice from './MiddleOffice';

describe('MiddleOffice Page', () => {
  it('renders the welcome heading', () => {
    renderWithRouter(<MiddleOffice />);
    expect(screen.getByText(/Welcome to the Trade Platform/i)).toBeInTheDocument();
  });
});
