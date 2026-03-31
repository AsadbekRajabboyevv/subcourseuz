import { TemplateRef } from '@angular/core';

// ─── Forma konfiguratsiyasi ───────────────────────────────────────────────────

export interface ValidatorConfig {
  type: 'minLength' | 'maxLength' | 'pattern' | 'email' | 'custom';
  value?: unknown;
  message: string;
  fn?: (value: unknown) => boolean;
}

export interface DependsOnConfig {
  field: string;
  value: unknown;
}

export interface SelectOption {
  label: string;
  value: unknown;
}

export interface FieldConfig {
  key: string;
  type: 'input' | 'select' | 'checkbox' | 'textarea' | 'radio';
  label: string;
  placeholder?: string;
  required?: boolean;
  validators?: ValidatorConfig[];
  dependsOn?: DependsOnConfig;
  roles?: string[];
  options?: SelectOption[];
}

// ─── Jadval konfiguratsiyasi ──────────────────────────────────────────────────

export interface ColumnConfig {
  field: string;
  header: string;
  sortable?: boolean;
  filterable?: boolean;
  template?: TemplateRef<unknown>;
  roles?: string[];
}

export interface SortEvent {
  field: string;
  direction: 'asc' | 'desc';
}

// ─── Menyu ────────────────────────────────────────────────────────────────────

export interface MenuItem {
  id: string;
  label: string;
  icon?: string;
  route?: string;
  roles?: string[];
  source?: 'backend' | 'static';
  children?: MenuItem[];
}

// ─── Filtr ────────────────────────────────────────────────────────────────────

export interface FilterConfig {
  key: string;
  label: string;
  type: 'text' | 'select' | 'date-range' | 'checkbox';
  options?: SelectOption[];
}

export interface FilterQuery {
  search?: string;
  [key: string]: unknown;
}

// ─── Holat komponentlari ──────────────────────────────────────────────────────

export interface StepConfig {
  label: string;
  description?: string;
  valid?: boolean;
}

// ─── Autentifikatsiya ─────────────────────────────────────────────────────────

export interface UserInfo {
  id: number;
  email: string;
  role: string;
  permissions: string[];
}

export interface AuthCredentials {
  email: string;
  password: string;
}

// ─── Bildirishnoma ────────────────────────────────────────────────────────────

export interface ToastMessage {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  createdAt: number;
}

// ─── Modal ────────────────────────────────────────────────────────────────────

export interface ModalConfig {
  title?: string;
  size?: 'sm' | 'md' | 'lg' | 'xl';
  closable?: boolean;
  data?: Record<string, unknown>;
}

export interface ConfirmConfig {
  title: string;
  message: string;
  confirmLabel?: string;
  cancelLabel?: string;
}

// ─── Fayl yuklash ─────────────────────────────────────────────────────────────

export type FileRejectionReason = 'type' | 'size';
