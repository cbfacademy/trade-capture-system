import { screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { renderWithRouter } from '../utils/test-utils';
import SignIn from './SignIn';

describe('SignIn Page', () => {
  it('renders the Sign In heading', () => {
    renderWithRouter(<SignIn />);
    expect(screen.getByText(/Sign In/i)).toBeInTheDocument();
  });
});
