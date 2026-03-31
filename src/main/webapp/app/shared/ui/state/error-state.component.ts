import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-error-state',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './error-state.component.html',
})
export class ErrorStateComponent {
  @Input() title = "Xatolik yuz berdi";
  @Input() message = "Ma'lumotlarni yuklashda xatolik yuz berdi. Iltimos, keyinroq qayta urinib ko'ring.";
  @Input() retryLabel = 'Qayta urinish';

  @Output() retry = new EventEmitter<void>();

  protected onRetry(): void {
    this.retry.emit();
  }
}

