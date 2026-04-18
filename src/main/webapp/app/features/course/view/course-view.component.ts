import {Component, inject, OnInit, signal} from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import {CourseInfo} from "../course.model";
import {Lesson} from "../../lesson/lesson.model";
import {CourseService} from "../course.service";
import {PageWrapperComponent} from "../../../shared/ui/layout/page-wrapper.component";
import {PaymentService} from "../../payment/payment.service";
import {PaymentModalComponent} from "../../payment/modal/payment-modal.component";
import {PaymentRequestDto} from "../../payment/payment.model";
import {AuthService} from "../../../common/auth/auth.service";

@Component({
  selector: 'app-course-view',
  standalone: true,
  imports: [CommonModule, RouterModule, PageWrapperComponent, PaymentModalComponent],
  templateUrl: './course-view.component.html',
  styles: `
    .description-container {
      word-break: break-word !important;
      overflow-wrap: break-word !important;
      white-space: normal !important;
      display: block !important;
      max-width: 100% !important;
    }

    .description-container ::ng-deep * {
      white-space: normal !important;
      word-break: break-word !important;
      max-width: 100% !important;
    }
  `
})
export class CourseViewComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private courseService = inject(CourseService);
  private paymentService = inject(PaymentService);
  showPaymentModal = signal<boolean>(false);
  isLoading = signal<boolean>(true);
  course = signal<CourseInfo | null>(null);
  protected authService = inject(AuthService);

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const courseId = params['id'];
      if (courseId) {
        this.loadCourse(courseId);
      }
    });
  }

  loadCourse(id: number) {
    this.isLoading.set(true);
    this.courseService.getById(id).subscribe({
      next: (res) => {
        this.course.set(res.data);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
      }
    });
  }

  openLesson(lesson: Lesson) {
    const currentCourse = this.course();
    if (!currentCourse?.purchased) return;

    const courseSlug = this.slugify(currentCourse.name);
    const lessonSlug = this.slugify(lesson.name);

    this.router.navigate([`/course/view/${courseSlug}/lesson/view/${lessonSlug}`], {
      queryParams: { cId: currentCourse.id, lId: lesson.id }
    });
  }
  openBuyModal() {
    this.showPaymentModal.set(true);
  }

  handlePayment(event: { couponCode: string }) {
    const currentCourse = this.course();
    if (!currentCourse) return;

    const request: PaymentRequestDto = {
      courseId: currentCourse.id,
      amount: currentCourse.price,
      couponCode: event.couponCode
    };

    this.paymentService.purchase(request).subscribe({
      next: (res) => {
        this.loadCourse(currentCourse.id);
        this.closeModal();
      }
    });
  }
  closeModal() {
    this.showPaymentModal.set(false);
  }

  private slugify(text: string): string {
    return text.toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/(^-|-$)+/g, '');
  }
}
