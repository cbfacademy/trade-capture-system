import { screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import { renderWithRouter } from '../utils/test-utils';
import ProtectedRoute from './ProtectedRoute';

describe('ProtectedRoute Component', () => {
  it('renders children when allowed', () => {
    renderWithRouter(
      <ProtectedRoute isAllowed={true} redirectPath={"/unauthorized"}>
        <div>Allowed Content</div>
      </ProtectedRoute>
    );

    expect(screen.getByText('Allowed Content')).toBeInTheDocument();
  });
});
