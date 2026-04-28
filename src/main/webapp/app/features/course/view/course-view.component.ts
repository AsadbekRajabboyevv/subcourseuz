import {Component, inject, OnInit, signal} from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import {CourseInfo} from "../course.model";
import {Lesson} from "../../lesson/lesson.model";
import {CourseService} from "../course.service";
import {PageWrapperComponent} from "../../../shared/ui/layout/page-wrapper.component";
import {PaymentService} from "../../payment/payment.service";
import {PaymentModalComponent} from "../../payment/modal/payment-modal.component";
import {PaymentRequest} from "../../payment/payment.model";
import {AuthService} from "../../../common/auth/auth.service";
import {MarkdownComponent} from "ngx-markdown";

@Component({
  selector: 'app-course-view',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    PageWrapperComponent,
    PaymentModalComponent,
    MarkdownComponent
  ],
  templateUrl: './course-view.component.html',
  styles: ``
})
export class CourseViewComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private courseService = inject(CourseService);
  private paymentService = inject(PaymentService);
  showPaymentModal = signal<boolean>(false);
  isLoading = signal<boolean>(true);
  course = signal<CourseInfo | null>(null);
  slug: string | null = null;
  protected authService = inject(AuthService);

  ngOnInit() {
    this.slug = this.route.snapshot.paramMap.get('slug');
    this.loadCourse(this.slug!);
  }

  loadCourse(slug: string) {
    this.isLoading.set(true);
    this.courseService.getById(slug).subscribe({
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
      queryParams: { cId: currentCourse.slug, lId: lesson.id }
    });
  }
  openBuyModal() {
    this.showPaymentModal.set(true);
  }

  handlePayment(event: { couponCode: string }) {
    const currentCourse = this.course();
    if (!currentCourse) return;

    const request: PaymentRequest = {
      courseSlug: currentCourse.slug,
      amount: currentCourse.price,
      couponCode: event.couponCode
    };

    this.paymentService.purchase(request).subscribe({
      next: (res) => {
        this.loadCourse(currentCourse.slug);
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
