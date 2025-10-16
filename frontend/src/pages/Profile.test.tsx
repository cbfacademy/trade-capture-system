import { screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { renderWithRouter } from '../utils/testUtils';
import Profile from './Profile';

describe('Profile Page', () => {
  it('renders the Profile heading', () => {
    renderWithRouter(<Profile />);
    expect(screen.getByText(/Profile/i)).toBeInTheDocument();
  });
});
