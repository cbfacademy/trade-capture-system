import { describe, it, expect, vi } from 'vitest';
import { render } from '@testing-library/react';
import AGGridTable from './AGGridTable';
import React from 'react';

// Mock AG Grid's module registration
vi.mock('ag-grid-react', () => ({
  AgGridReact: () => <div data-testid="ag-grid" className="ag-root-wrapper">AG Grid Mock</div>
}));

describe('AGGridTable Component', () => {
  it('renders the AG Grid container', () => {
    const columnDefs = [{ headerName: 'Test', field: 'test' }];
    const rowData = [{ test: 'value' }];

    const { getByTestId } = render(<AGGridTable columnDefs={columnDefs} rowData={rowData} rowSelection={'single'} />);
    // Check for the test ID instead of the class
    expect(getByTestId('ag-grid')).toBeInTheDocument();
  });
});
