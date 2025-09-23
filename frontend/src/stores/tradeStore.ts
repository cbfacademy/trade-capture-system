import { makeAutoObservable } from 'mobx';

class TradeStore {
  trades = [];
  isLoading = false;
  error = null;

  constructor() {
    makeAutoObservable(this);
  }

  setTrades(trades) {
    this.trades = trades;
  }

  addTrade(trade) {
    this.trades.push(trade);
  }

  setLoading(isLoading) {
    this.isLoading = isLoading;
  }

  setError(error) {
    this.error = error;
  }
}

const tradeStore = new TradeStore();
export default tradeStore;

