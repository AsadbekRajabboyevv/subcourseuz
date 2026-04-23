export interface PaymentRequest {
  courseId?: number;
  testId?: number;
  amount: number;
  couponCode?: string;
}

export interface Payment {
  exId: string;
  status: PaymentStatus;
  transactionId: string;
  amount: number;
  currency: CurrencyEnum;
  type: PaymentType;
}

export enum PaymentStatus {
  CREATED = 'CREATED',
  PROCESSING = 'PROCESSING',
  SUCCESS = 'SUCCESS',
  FAILED = 'FAILED',
  CANCELLED = 'CANCELLED'
}

export enum PaymentType {
  COURSE = 'COURSE',
  TEST = 'TEST',
  DEBIT = 'DEBIT',
  TOP_UP = 'TOP_UP'
}

export enum CurrencyEnum {
  UZS = 'UZS',
  USD = 'USD',
  RUB = 'RUB'
}

export interface PaymentFilter {
  status?: PaymentStatus;
  type?: PaymentType;
  fromAmount?: number;
  toAmount?: number;
}
