import { CommonModule } from '@angular/common';
import { Component, EventEmitter, HostListener, Input, Output, effect } from '@angular/core';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './modal.component.html',
})
export class ModalComponent {
  @Input() title = '';
  @Input() size: 'sm' | 'md' | 'lg' | 'xl' = 'md';
  @Input() closable = true;

  @Output() closed = new EventEmitter<void>();

  constructor() {
    effect(() => {
      document.documentElement.classList.add('overflow-hidden');
    });
  }

  @HostListener('document:keydown.escape', ['$event'])
  onEscape(event: Event): void {
    const keyboardEvent = event as KeyboardEvent;

    if (!this.closable) return;
    keyboardEvent.preventDefault();
    this.close();
  }

  close(): void {
    if (!this.closable) return;
    this.closed.emit();
    document.documentElement.classList.remove('overflow-hidden');
  }

  onBackdropClick(event: MouseEvent): void {
    if (event.target === event.currentTarget && this.closable) {
      this.close();
    }
  }

  protected get sizeClasses(): string {
    switch (this.size) {
      case 'sm':
        return 'max-w-md';
      case 'lg':
        return 'max-w-3xl';
      case 'xl':
        return 'max-w-5xl';
      default:
        return 'max-w-lg';
    }
  }
}

