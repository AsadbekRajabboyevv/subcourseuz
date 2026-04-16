import { Component, EventEmitter, Input, Output} from '@angular/core';
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {InputComponent} from "../../../shared/ui/forms/input.component";
@Component({
  selector: 'app-payment-modal',
  templateUrl: './payment-modal.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, InputComponent]
})

export class PaymentModalComponent {
  @Input({ required: true }) item: any;
  @Input() type: 'COURSE' | 'TEST' = 'COURSE';

  @Output() close = new EventEmitter<void>();
  @Output() confirm = new EventEmitter<{ couponCode: string }>();

  couponCode = '';

  onConfirm() {
    this.confirm.emit({ couponCode: this.couponCode });
  }

  onClose() {
    this.close.emit();
  }

}
