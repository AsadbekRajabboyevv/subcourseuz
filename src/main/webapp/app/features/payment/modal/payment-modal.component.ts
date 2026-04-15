import { Component, EventEmitter, Input, Output, inject, signal } from '@angular/core';
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {InputComponent} from "../../../shared/ui/forms/input.component";
import {PaymentService} from "../payment.service";
import {PaymentRequestDto} from "../payment.model";

@Component({
  selector: 'app-payment-modal',
  templateUrl: './payment-modal.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, InputComponent]
})
export class PaymentModalComponent {
  private paymentService = inject(PaymentService);

  @Input() itemData: any;
  @Input() type: 'COURSE' | 'TEST' = 'COURSE';
  @Output() close = new EventEmitter<void>();
  @Output() success = new EventEmitter<any>();

  couponCode = signal('');
  isLoading = signal(false);

  confirmPurchase() {
    this.isLoading.set(true);

    const request: PaymentRequestDto = {
      courseId: this.type === 'COURSE' ? this.itemData.id : null,
      testId: this.type === 'TEST' ? this.itemData.id : null,
      amount: this.itemData.price,
      couponCode: this.couponCode()
    };

    this.paymentService.purchase(request).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.success.emit(res.data);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
