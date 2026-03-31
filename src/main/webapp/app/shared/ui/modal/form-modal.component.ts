import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalComponent } from './modal.component';
import { DynamicFormComponent } from '../forms/dynamic-form/dynamic-form.component';
import { FieldConfig } from '../interfaces';

@Component({
  selector: 'app-form-modal',
  standalone: true,
  imports: [CommonModule, ModalComponent, DynamicFormComponent],
  templateUrl: './form-modal.component.html',
})
export class FormModalComponent {
  @Input() title = '';
  @Input() fields: FieldConfig[] = [];
  @Input() submitLabel = 'Saqlash';

  @Output() closed = new EventEmitter<void>();
  @Output() submitted = new EventEmitter<Record<string, unknown>>();

  onClose(): void {
    this.closed.emit();
  }

  onFormSubmit(value: Record<string, unknown>): void {
    this.submitted.emit(value);
  }
}

