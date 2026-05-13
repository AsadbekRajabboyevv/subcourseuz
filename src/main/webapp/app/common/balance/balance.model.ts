export enum CurrencyEnum {
  UZS = 'UZS',
  USD = 'USD',
  EUR = 'EUR'
}

export interface UserBalance {
  userId: number;
  userFullName: string;
  userEmail: string;
  balance: number;
  pendingBalance: number;
  totalBalance: number;
  currency: CurrencyEnum;
  lastTransactionAt: string | Date;
}
