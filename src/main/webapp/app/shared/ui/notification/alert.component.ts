import { Component, EventEmitter, Input, Output, signal, computed, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgIconsModule, provideIcons } from '@ng-icons/core';
import { heroCheckCircle, heroExclamationCircle, heroExclamationTriangle, heroInformationCircle, heroXCircle, heroXMark } from '@ng-icons/heroicons/outline';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [CommonModule, NgIconsModule],
  providers: [provideIcons({ heroCheckCircle, heroXCircle, heroExclamationTriangle, heroInformationCircle, heroExclamationCircle, heroXMark })],
  templateUrl: './alert.component.html',
})
export class AlertComponent implements OnInit, OnDestroy {
  @Input() type: 'success' | 'error' | 'warning' | 'info' = 'info';
  @Input() message = '';
  @Input() dismissible = true;
  @Input() duration = 5000; // 5 soniyadan keyin o'zi yo'qoladi

  @Output() dismissed = new EventEmitter<void>();

  private timeoutId?: any;

  ngOnInit() {
    if (this.duration > 0) {
      this.timeoutId = setTimeout(() => this.close(), this.duration);
    }
  }

  ngOnDestroy() {
    if (this.timeoutId) clearTimeout(this.timeoutId);
  }

  protected iconName = computed(() => {
    switch (this.type) {
      case 'success': return 'heroCheckCircle';
      case 'error': return 'heroXCircle';
      case 'warning': return 'heroExclamationTriangle';
      case 'info': default: return 'heroInformationCircle';
    }
  });

  protected close(): void {
    this.dismissed.emit();
  }

  protected get containerClasses(): string {
    // FIXED va Z-INDEX qo'shildi. Joylashuvi: o'ng tepa burchak.
    const base = 'fixed top-6 right-6 z-[9999] flex items-center gap-4 rounded-2xl border-2 px-4 py-3.5 ' +
      'text-sm font-medium shadow-2xl backdrop-blur-md animate-in fade-in slide-in-from-right-5 duration-300 w-[350px]';

    switch (this.type) {
      case 'success':
        return `${base} border-green-500/30 bg-green-50/90 text-green-800 dark:bg-green-950/90 dark:text-green-400`;
      case 'error':
        return `${base} border-red-500/30 bg-red-50/90 text-red-800 dark:bg-red-950/90 dark:text-red-400`;
      case 'warning':
        return `${base} border-yellow-500/30 bg-yellow-50/90 text-yellow-800 dark:bg-yellow-950/90 dark:text-yellow-400`;
      case 'info':
      default:
        return `${base} border-blue-500/30 bg-blue-50/90 text-blue-800 dark:bg-blue-950/90 dark:text-blue-400`;
    }
  }
}
