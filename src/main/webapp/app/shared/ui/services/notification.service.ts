import { Injectable, signal } from '@angular/core';
import { ToastMessage } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly MAX_TOASTS = 5;

  toasts = signal<ToastMessage[]>([]);

  success(message: string): void {
    this.addToast({ type: 'success', message });
  }

  error(message: string): void {
    this.addToast({ type: 'error', message });
  }

  warning(message: string): void {
    this.addToast({ type: 'warning', message });
  }

  info(message: string): void {
    this.addToast({ type: 'info', message });
  }

  dismiss(id: string): void {
    this.toasts.update(list => list.filter(t => t.id !== id));
  }

  private addToast(partial: Pick<ToastMessage, 'type' | 'message'>): void {
    const toast: ToastMessage = {
      id: crypto.randomUUID(),
      type: partial.type,
      message: partial.message,
      createdAt: Date.now(),
    };

    this.toasts.update(list => {
      const updated = [...list, toast];
      // Remove oldest if over limit
      return updated.length > this.MAX_TOASTS ? updated.slice(updated.length - this.MAX_TOASTS) : updated;
    });
  }
}
