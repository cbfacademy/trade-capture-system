export interface TradeLeg {
  legId: string;
  legType: string;
  notional: number | string;
  currency: string;
  rate?: number | string;
  index?: string;
  calculationPeriodSchedule?: string;
  paymentBusinessDayConvention?: string;
  payReceiveFlag?: string;
  [key: string]: unknown;
}

export interface Trade {
  tradeId: string;
  version?: string;
  bookName?: string;
  counterpartyName?: string;
  traderUserName?: string;
  inputterUserName?: string;
  tradeType?: string;
  tradeSubType?: string;
  tradeStatus?: string;
  tradeDate?: string;
  startDate?: string;
  maturityDate?: string;
  executionDate?: string;
  utiCode?: string;
  lastTouchTimestamp?: string;
  validityStartDate?: string;
  tradeLegs: TradeLeg[];
  [key: string]: unknown;
}

export interface CashflowDTO {
  id?: number;
  paymentValue: number | string;
  valueDate: string;
  payRec: string;
  paymentType: string;
  paymentBusinessDayConvention?: string;
  rate?: number | string;
}

export interface CashflowGenerationLegDTO {
  legType: string;
  notional: number;
  rate?: number;
  index?: string;
  calculationPeriodSchedule?: string;
  paymentBusinessDayConvention?: string;
  payReceiveFlag?: string;
}
