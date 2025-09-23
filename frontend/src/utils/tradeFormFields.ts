import staticStore from "../stores/staticStore";

/**
 * Field definitions for trade form
 */
export const TRADE_FIELDS = [
    {key: "tradeId", label: "Trade ID", type: "input"},
    {key: "version", label: "Version", type: "input"},
    {key: "bookName", label: "Book", type: "dropdown", options: () => staticStore.bookValues ?? []},
    {
        key: "counterpartyName",
        label: "Counterparty",
        type: "dropdown",
        options: () => staticStore.counterpartyValues ?? []
    },
    {key: "traderUserName", label: "Trader", type: "dropdown", options: () => staticStore.userValues ?? []},
    {key: "inputterUserName", label: "Inputter", type: "dropdown", options: () => staticStore.userValues ?? []},
    {key: "tradeType", label: "Trade Type", type: "dropdown", options: () => staticStore.tradeTypeValues ?? []},
    {key: "tradeSubType", label: "Trade Sub Type", type: "dropdown", options: () => staticStore.tradeSubTypeValues ?? []},
    {key: "tradeStatus", label: "Trade Status", type: "dropdown", options: () => staticStore.tradeStatusValues ?? []},
    {key: "tradeDate", label: "Trade Date", type: "date"},
    {key: "startDate", label: "Start Date", type: "date"},
    {key: "maturityDate", label: "Maturity Date", type: "date"},
    {key: "executionDate", label: "Execution Date", type: "date"},
    {key: "utiCode", label: "UTI Code", type: "input"},
    {key: "lastTouchTimestamp", label: "Last Touch Timestamp", type: "date"},
    {key: "validityStartDate", label: "Version Active Since", type: "date"},
];

/**
 * Field definitions for trade leg form
 */
export const TRADE_LEG_FIELDS = [
    {key: "legId", label: "Leg ID", type: "input"},
    {key: "legType", label: "Leg Type", type: "dropdown", options: () => staticStore.legTypeValues ?? []},
    {key: "notional", label: "Notional Amount", type: "input"},
    {key: "currency", label: "Currency", type: "dropdown", options: () => staticStore.currencyValues ?? []},
    {key: "rate", label: "Fixed Rate", type: "input"},
    {key: "index", label: "Floating Rate Index", type: "dropdown", options: () => staticStore.indexValues ?? []},
    {
        key: "calculationPeriodSchedule",
        label: "Payment Frequency",
        type: "dropdown",
        options: () => staticStore.scheduleValues
    },
    {
        key: "paymentBusinessDayConvention",
        label: "Payment BDC",
        type: "dropdown",
        options: () => staticStore.businessDayConventionValues ?? []
    },
    {key: "payReceiveFlag", label: "Pay/Rec", type: "dropdown", options: () => staticStore.payRecValues ?? []},
];

// Constants for field behavior
export const DISABLED_FIELDS = ["tradeId", "version", "tradeStatus"];
export const DISABLED_LEG_FIELDS = ["legId"];
export const FLOATING_FIELDS = ["index"];
export const FIXED_FIELDS = ["rate"];
