import React from 'react';
import {AgGridReact} from 'ag-grid-react';
import {ColDef, SelectionChangedEvent} from 'ag-grid-community';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';

interface AGGridTableProps {
    columnDefs: ColDef[];
    rowData: unknown[];
    onSelectionChanged?: (event:SelectionChangedEvent) => void;
    rowSelection: 'single' | 'multiple';
    [key: string]: unknown;
}

const AGGridTable: React.FC<AGGridTableProps> = (props) => {
    const {columnDefs, rowData, onSelectionChanged, rowSelection, ...rest} = props;

    return (
        <div className={"h-fit w-full ag-theme-alpine"}>
            <AgGridReact
                columnDefs={columnDefs}
                rowData={rowData}
                domLayout="autoHeight"
                rowSelection={rowSelection}
                theme={"legacy"}
                onSelectionChanged={onSelectionChanged}
                {...rest}
            />
        </div>
    );
};

export default AGGridTable;
