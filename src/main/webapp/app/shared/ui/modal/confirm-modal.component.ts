import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ConfirmConfig } from '../interfaces';
import { ModalComponent } from './modal.component';

@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  imports: [CommonModule, ModalComponent],
  templateUrl: './confirm-modal.component.html',
})
export class ConfirmModalComponent {
  @Input() config!: ConfirmConfig;

  @Output() confirmed = new EventEmitter<boolean>();

  onClose(): void {
    this.confirmed.emit(false);
  }

  onConfirm(): void {
    this.confirmed.emit(true);
  }
}

