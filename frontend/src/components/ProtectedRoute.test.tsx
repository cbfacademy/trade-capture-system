import { describe, it, expect } from 'vitest';
import { screen } from '@testing-library/react';
import ProtectedRoute from './ProtectedRoute';
import React from 'react';
import { renderWithRouter } from '../utils/test-utils';

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
