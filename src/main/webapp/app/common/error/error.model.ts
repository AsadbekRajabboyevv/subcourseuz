export interface ErrorData {
  title: string;
  message: string;
  code?: string;
  type?: 'error' | 'warning' | 'info';
}
