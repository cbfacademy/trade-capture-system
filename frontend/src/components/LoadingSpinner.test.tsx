import * as matchers from '@testing-library/jest-dom/matchers';
import { render } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import LoadingSpinner from './LoadingSpinner';

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
