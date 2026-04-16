import { Component, OnInit, inject, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService } from "../course.service";
import { InputComponent } from "../../../shared/ui/forms/input.component";
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";
import { CourseFilter, DurationType } from "../course.model";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { PaymentService } from "../../payment/payment.service";
import { PaymentModalComponent } from "../../payment/modal/payment-modal.component";
import { AuthService } from "../../../common/auth/auth.service";

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [CommonModule, FormsModule, InputComponent, PageWrapperComponent, RouterLink, PaymentModalComponent],
  templateUrl: './course-list.component.html'
})
export class CourseListComponent implements OnInit {
  public courseService = inject(CourseService);
  private paymentService = inject(PaymentService);
  private router = inject(Router);
  protected authService = inject(AuthService);
  private route = inject(ActivatedRoute);

  page = 0;
  size = 10;
  hasMore = true;
  loading = false;
  showPaymentModal = false;
  selectedItem: any = null;
  paymentType: 'COURSE' | 'TEST' = 'COURSE';
  couponCode = '';

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
    this.applyFilters(); // Birinchi yuklash

    this.route.queryParams.subscribe(params => {
      const buyNow = params['buyNow'];
      const courseId = params['id'];
      if (buyNow === 'true' && courseId) {
        this.handleAutoOpenAfterLogin(Number(courseId));
      }
    });
  }

  // Window emas, capture mode orqali istalgan scrollni eshitish
  // Variant 1: Eventni qabul qiladigan qilish (Tavsiya etiladi)
  @HostListener('window:scroll', ['$event'])
  @HostListener('document:scroll', ['$event'])
  onScroll(event?: Event) { // <-- Bu yerga parametr qo'shildi
    const pos = (document.documentElement.scrollTop || document.body.scrollTop) + document.documentElement.offsetHeight;
    const max = document.documentElement.scrollHeight;

    if (pos >= max - 200) {
      if (!this.loading && this.hasMore) {
        this.loadData();
      }
    }
  }
  loadData() {
    if (this.loading || !this.hasMore) return;

    this.loading = true;
    console.log(`Yuklanmoqda: Sahifa ${this.page}`);

    this.courseService.get(this.filter, this.page, this.size)
    .subscribe({
      next: (res) => {
        const newData = res.data.content;

        if (this.page === 0) {
          this.courseService.setCourses(newData);
        } else {
          this.courseService.appendCourses(newData);
        }

        this.hasMore = !res.data.last;
        this.page++;
        this.loading = false;

        // Agar yuklangan ma'lumot ekranni to'ldirmasa, yana bitta sahifa yuklash
        this.checkIfNeedMoreContent();
      },
      error: (err) => {
        console.error("Yuklashda xato:", err);
        this.loading = false;
      }
    });
  }

  // Ekran to'lmagan bo'lsa avtomatik keyingi sahifani chaqirish
  private checkIfNeedMoreContent() {
    setTimeout(() => {
      if (document.documentElement.scrollHeight <= window.innerHeight && this.hasMore) {
        this.loadData();
      }
    }, 500);
  }

  applyFilters() {
    this.page = 0;
    this.hasMore = true;
    this.courseService.setCourses([]); // Eski ma'lumotlarni tozalash
    this.loadData();
  }

  resetFilters() {
    this.filter = { search: '' };
    this.applyFilters();
  }

  // --- To'lov va Modal mantiqi (O'zgarishsiz) ---
  onBuy(item: any, type: 'COURSE' | 'TEST' = 'COURSE') {
    if (!this.authService.isLoggedIn()) {
      const currentUrl = this.router.url.split('?')[0];
      const returnUrl = `${currentUrl}?id=${item.id}`;
      this.router.navigate(['/auth/login'], { queryParams: { returnUrl: returnUrl } });
      return;
    }
    this.selectedItem = item;
    this.paymentType = type;
    this.showPaymentModal = true;
  }

  closeModal() {
    this.showPaymentModal = false;
    this.selectedItem = null;
    this.couponCode = '';
  }

  handlePayment(event: { couponCode: string }) {
    if (!this.selectedItem) return;
    const request = {
      courseId: this.paymentType === 'COURSE' ? this.selectedItem.id : undefined,
      testId: this.paymentType === 'TEST' ? this.selectedItem.id : undefined,
      amount: this.selectedItem.price,
      couponCode: event.couponCode
    };
    this.paymentService.purchase(request).subscribe({
      next: () => { this.closeModal(); }
    });
  }

  private handleAutoOpenAfterLogin(courseId: number) {
    const checkInterval = setInterval(() => {
      const course = this.courseService.courses().find(c => c.id === courseId);
      if (course) {
        this.selectedItem = course;
        this.paymentType = 'COURSE';
        this.showPaymentModal = true;
        this.router.navigate([], {
          relativeTo: this.route,
          queryParams: { buyNow: null, id: null },
          queryParamsHandling: 'merge'
        });
        clearInterval(checkInterval);
      }
    }, 200);
    setTimeout(() => clearInterval(checkInterval), 4000);
  }
}
