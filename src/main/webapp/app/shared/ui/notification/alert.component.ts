import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alert.component.html',
})
export class AlertComponent {
  @Input() type: 'success' | 'error' | 'warning' | 'info' = 'info';
  @Input() message = '';
  @Input() dismissible = false;

  @Output() dismissed = new EventEmitter<void>();

  protected close(): void {
    this.dismissed.emit();
  }

  protected get containerClasses(): string {
    const base =
      'flex items-start gap-2 rounded-lg border px-3 py-2 text-sm';

    switch (this.type) {
      case 'success':
        return `${base} border-green-100 bg-green-50 text-green-800 dark:border-green-900 dark:bg-green-950 dark:text-green-200`;
      case 'error':
        return `${base} border-red-100 bg-red-50 text-red-800 dark:border-red-900 dark:bg-red-950 dark:text-red-200`;
      case 'warning':
        return `${base} border-yellow-100 bg-yellow-50 text-yellow-800 dark:border-yellow-900 dark:bg-yellow-950 dark:text-yellow-200`;
      case 'info':
      default:
        return `${base} border-blue-100 bg-blue-50 text-blue-800 dark:border-blue-900 dark:bg-blue-950 dark:text-blue-200`;
    }
  }
}

