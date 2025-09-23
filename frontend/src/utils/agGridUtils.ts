export function getColDefFromResult(data: unknown): unknown[] {
    if (!data) return [];
    const sample = Array.isArray(data) ? data[0] : data;
    if (!sample) return [];
    return Object.keys(sample).map(key => {
        if (key === 'active') {
            return {
                headerName: key.charAt(0).toUpperCase() + key.slice(1),
                field: key,
                cellRenderer: 'agCheckboxCellRenderer',
                cellRendererParams: { tristate: false }, // display only, not for selection
                sortable: true,
                filter: false,
                width: 90
            };
        }
        return {
            headerName: key.charAt(0).toUpperCase() + key.slice(1),
            field: key,
            sortable: true,
            filter: false
        };
    });
}


export function getRowDataFromData(data: unknown): unknown[] {
    if (!data) return [];
    const arr = Array.isArray(data) ? data : typeof data === 'object' ? [data] : [];
    return arr.map((row: Record<string, unknown>) => {
        let activeValue = row.active;
        if (typeof activeValue !== 'boolean') {
            if (activeValue === 'true' || activeValue === 1 || activeValue === '1') {
                activeValue = true;
            } else {
                activeValue = false;
            }
        }
        return {
            ...row,
            active: activeValue
        };
    });
}
