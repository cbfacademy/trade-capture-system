import { describe, it, expect } from 'vitest';
import { screen } from '@testing-library/react';
import SignIn from './SignIn';
import React from 'react';
import { renderWithRouter } from '../utils/test-utils';

describe('SignIn Page', () => {
  it('renders the Sign In heading', () => {
    renderWithRouter(<SignIn />);
    expect(screen.getByText(/Sign In/i)).toBeInTheDocument();
  });
});
