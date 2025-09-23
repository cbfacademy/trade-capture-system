import { describe, it, expect } from 'vitest';
import { screen } from '@testing-library/react';
import Sidebar from './Sidebar';
import React from 'react';
import { renderWithRouter } from '../utils/test-utils';

describe('Sidebar Component', () => {
  it('renders the sidebar container', () => {
    renderWithRouter(<Sidebar />);
    // Check for the sidebar element by role or class
    expect(screen.getByRole('complementary')).toBeInTheDocument();
  });
});
