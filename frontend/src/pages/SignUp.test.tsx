import { describe, it, expect, vi } from 'vitest';
import { screen } from '@testing-library/react';
import SignUp from './SignUp';
import React from 'react';
import { renderWithQueryClient } from '../utils/test-utils';

vi.mock('../utils/api', () => ({
  fetchUserProfiles: vi.fn().mockResolvedValue({ data: [] }),
  createUser: vi.fn().mockResolvedValue({}),
}));

describe('SignUp Modal', () => {
  it('renders form fields when open', () => {
    renderWithQueryClient(<SignUp isOpen={true} onClose={() => {}} />);
    // Use heading role to be more specific
    expect(screen.getByRole('heading', { name: /Sign Up/i })).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/First Name/i)).toBeInTheDocument();
  });
});
