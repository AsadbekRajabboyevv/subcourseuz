export interface ErrorData {
  title: string;
  message: string;
  fieldErrorMessages?: { [key: string]: string | string[] };
  code?: string;
  type?: 'error' | 'warning' | 'info';
}
