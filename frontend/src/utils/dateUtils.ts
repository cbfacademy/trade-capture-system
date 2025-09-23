/**
 * Date utilities for trade operations
 */

/**
 * Get today's date in yyyy-mm-dd format
 * @returns {string} Today's date as yyyy-mm-dd
 */
export const getToday = (): string => {
    const d = new Date();
    return d.toISOString().slice(0, 10);
};

/**
 * Get a date one year from today in yyyy-mm-dd format
 * @returns {string} Date one year from today as yyyy-mm-dd
 */
export const getOneYearFromToday = (): string => {
    const d = new Date();
    d.setFullYear(d.getFullYear() + 1);
    return d.toISOString().slice(0, 10);
};

/**
 * Format a date for backend API (ISO8601 with time)
 * @param d - Date to format
 * @returns {string|null} Formatted date string or null
 */
export const formatDateForBackend = (d: string | Date | undefined | null): string | null => {
    if (!d) return null;
    if (typeof d === 'string' && d.includes('T')) return d;
    if (typeof d === 'string') return d + 'T00:00:00';
    if (typeof d === 'object') return d.toISOString();
    return d as string;
};

interface TradeData {
    tradeDate?: string;
    startDate?: string;
    maturityDate?: string;
    executionDate?: string;
    lastTouchTimestamp?: string;
    validityStartDate?: string;
    [key: string]: unknown; // Allow for additional properties
}

/**
 * Format dates from backend API response for display
 * @param trade - Trade data from backend
 * @returns Trade with properly formatted dates
 */
export const formatDatesFromBackend = (trade: TradeData): TradeData => {
    // Convert any full ISO date strings (with time) to just the date portion (YYYY-MM-DD)
    if (trade.tradeDate && trade.tradeDate.includes('T')) {
        trade.tradeDate = trade.tradeDate.split('T')[0];
    }
    if (trade.startDate && trade.startDate.includes('T')) {
        trade.startDate = trade.startDate.split('T')[0];
    }
    if (trade.maturityDate && trade.maturityDate.includes('T')) {
        trade.maturityDate = trade.maturityDate.split('T')[0];
    }
    if (trade.executionDate && trade.executionDate.includes('T')) {
        trade.executionDate = trade.executionDate.split('T')[0];
    }
    if (trade.lastTouchTimestamp && trade.lastTouchTimestamp.includes('T')) {
        trade.lastTouchTimestamp = trade.lastTouchTimestamp.split('T')[0];
    }
    if (trade.validityStartDate && trade.validityStartDate.includes('T')) {
        trade.validityStartDate = trade.validityStartDate.split('T')[0];
    }
    if (trade.validityEndDate && trade.validityEndDate.includes('T')) {
        trade.validityEndDate = trade.validityEndDate.split('T')[0];
    }
    return trade;
};
