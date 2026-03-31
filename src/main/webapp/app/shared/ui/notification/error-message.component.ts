import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-error-message',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './error-message.component.html',
})
export class ErrorMessageComponent {
  @Input() message = "Nimadir noto'g'ri bajarildi. Iltimos, keyinroq qayta urinib ko'ring.";
  @Input() retryLabel = 'Qayta urinish';

  @Output() retry = new EventEmitter<void>();

  protected onRetry(): void {
    this.retry.emit();
  }
}

