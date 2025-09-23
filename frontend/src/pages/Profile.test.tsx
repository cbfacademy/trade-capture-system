import { describe, it, expect } from 'vitest';
import { screen } from '@testing-library/react';
import Profile from './Profile';
import React from 'react';
import { renderWithRouter } from '../utils/test-utils';

describe('Profile Page', () => {
  it('renders the Profile heading', () => {
    renderWithRouter(<Profile />);
    expect(screen.getByText(/Profile/i)).toBeInTheDocument();
  });
});
