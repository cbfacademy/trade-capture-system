import { screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { renderWithRouter } from '../utils/test-utils';
import Sidebar from './Sidebar';

describe('Sidebar Component', () => {
  it('renders the sidebar container', () => {
    renderWithRouter(<Sidebar />);
    // Check for the sidebar element by role or class
    expect(screen.getByRole('complementary')).toBeInTheDocument();
  });
});
