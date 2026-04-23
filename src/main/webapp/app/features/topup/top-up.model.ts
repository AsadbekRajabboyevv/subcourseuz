import {BaseAudit} from "../../common/model/base";

export enum TopUpStatus {
  CREATED = 'CREATED',
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED'
}

export enum PaymentStatus {
  CREATED = 'CREATED',
  PROCESSING = 'PROCESSING',
  SUCCESS = 'SUCCESS',
  FAILED = 'FAILED',
  CANCELLED = 'CANCELLED'
}

export enum CurrencyEnum {
  UZS = 'UZS',
  USD = 'USD',
  RUB = 'RUB'
}

export enum PaymentType {
  COURSE = 'COURSE',
  TEST = 'TEST',
  DEBIT = 'DEBIT',
  TOP_UP = 'TOP_UP'
}
export interface TopUpRequest extends BaseAudit{
  id: number;
  amount: number;
  message: string;
  status: TopUpStatus;
  transactionId: string;
  userId: number;
  userFullName: string;
  fileKey: string;
  comment: string;
  approvedBy?: number;
  approvedByFullName?: string;
  approvedAt?: Date;
  rejectedAt?: Date;
  paymentExId?: string;
}

export interface TopUpBalance {
  amount: number;
  message?: string | null;
}

export interface TopUpRequestAction {
  id: number;
  message: string;
  paymentExId: string;
}

export interface TopUpRequestFilter {
  transactionId?: string;
  message?: string;
  status?: TopUpStatus;
  amountTo?: number;
  amountFrom?: number;
}

