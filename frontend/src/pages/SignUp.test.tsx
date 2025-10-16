import { screen } from '@testing-library/react';
import { describe, expect, it, vi } from 'vitest';
import { renderWithQueryClient } from '../utils/test-utils';
import SignUp from './SignUp';

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
