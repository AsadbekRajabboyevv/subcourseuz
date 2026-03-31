import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { NotificationService } from '../services/notification.service';
import { ToastMessage } from '../interfaces';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast-container.component.html',
})
export class ToastContainerComponent implements OnInit, OnDestroy {
  private readonly notificationService = inject(NotificationService);
  protected toasts = this.notificationService.toasts;

  private timerId?: number;

  ngOnInit(): void {
    this.startAutoDismiss();
  }

  ngOnDestroy(): void {
    if (this.timerId !== undefined) {
      window.clearInterval(this.timerId);
    }
  }

  protected dismiss(id: string): void {
    this.notificationService.dismiss(id);
  }

  private startAutoDismiss(): void {
    const AUTO_DISMISS_MS = 4000;
    this.timerId = window.setInterval(() => {
      const now = Date.now();
      const list = this.toasts();
      list
        .filter(t => now - t.createdAt >= AUTO_DISMISS_MS)
        .forEach(t => this.notificationService.dismiss(t.id));
    }, 500);
  }

  protected toastClasses(toast: ToastMessage): string {
    const base =
      'pointer-events-auto flex w-80 items-start gap-3 rounded-lg border px-3 py-2 text-sm shadow-sm bg-white dark:bg-gray-900';

    switch (toast.type) {
      case 'success':
        return `${base} border-green-100 text-green-700 dark:border-green-900 dark:text-green-300`;
      case 'error':
        return `${base} border-red-100 text-red-700 dark:border-red-900 dark:text-red-300`;
      case 'warning':
        return `${base} border-yellow-100 text-yellow-700 dark:border-yellow-900 dark:text-yellow-300`;
      case 'info':
      default:
        return `${base} border-blue-100 text-blue-700 dark:border-blue-900 dark:text-blue-300`;
    }
  }
}

