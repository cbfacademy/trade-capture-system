import { describe, it, expect } from 'vitest';
import { render } from '@testing-library/react';
import * as matchers from '@testing-library/jest-dom/matchers';
import LoadingSpinner from './LoadingSpinner';
import React from 'react';

expect.extend(matchers);

describe('LoadingSpinner Component', () => {
  it('renders the spinner svg', () => {
    const { container } = render(<LoadingSpinner />);
    expect(container.querySelector('svg')).toBeInTheDocument();
  });
  it('renders without crashing', () => {
    render(<LoadingSpinner />);
    // Test passes if component renders without throwing
  });
});
