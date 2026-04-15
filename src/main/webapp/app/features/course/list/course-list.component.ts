import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {CourseService} from "../course.service";
import {InputComponent} from "../../../shared/ui/forms/input.component";
import {PageWrapperComponent} from "../../../shared/ui/layout/page-wrapper.component";
import {CourseFilter, DurationType} from "../course.model";
import {RouterLink} from "@angular/router";
import {PaymentRequestDto} from "../../payment/payment.model";
import {PaymentService} from "../../payment/payment.service";

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [CommonModule, FormsModule, InputComponent, PageWrapperComponent, RouterLink],
  templateUrl: './course-list.component.html'
})
export class CourseListComponent implements OnInit {
  public courseService = inject(CourseService);
  private paymentService = inject(PaymentService);
  showPaymentModal = false;
  selectedItem: any = null;
  paymentType: 'COURSE' | 'TEST' = 'COURSE';
  couponCode = ''

  filter: CourseFilter = {
    search: '',
    scienceId: undefined,
    gradeId: undefined,
    priceFrom: undefined,
    priceTo: undefined,
    lang: '',
    durationType: undefined,
    duration: undefined
  };

  durationTypes = Object.entries(DurationType).map(([key, value]) => ({
    label: key,
    value: value
  }));

  languages = [
    { label: 'O\'zbekcha', value: 'UZ' },
    { label: 'Ruscha', value: 'RU' },
    { label: 'Inglizcha', value: 'EN' }
  ];

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.courseService.get(this.filter).subscribe();
  }

  resetFilters() {
    this.filter = { search: '' };
    this.loadData();
  }

  onBuy(item: any, type: 'COURSE' | 'TEST' = 'COURSE') {
    this.selectedItem = item;
    this.paymentType = type;
    this.showPaymentModal = true;
  }

  // Modalni yopish
  closeModal() {
    this.showPaymentModal = false;
    this.selectedItem = null;
    this.couponCode = '';
  }

  confirmPayment() {
    if (!this.selectedItem) return;

    const request: PaymentRequestDto = {
      courseId: this.paymentType === 'COURSE' ? this.selectedItem.id : undefined,
      testId: this.paymentType === 'TEST' ? this.selectedItem.id : undefined,
      amount: this.selectedItem.price,
      couponCode: this.couponCode
    };

    this.paymentService.purchase(request).subscribe({
      next: (res) => {
        console.log('To\'lov muvaffaqiyatli:', res.data);
        this.closeModal();
      }
    });
  }
}
