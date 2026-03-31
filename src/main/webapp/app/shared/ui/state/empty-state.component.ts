import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './empty-state.component.html',
})
export class EmptyStateComponent {
  @Input() title = "Ma'lumotlar topilmadi";
  @Input() description = "Hozircha ko'rsatish uchun ma'lumotlar yo'q.";
  @Input() icon?: string;
  @Input() actionLabel?: string;

  @Output() action = new EventEmitter<void>();

  protected onAction(): void {
    this.action.emit();
  }
}

