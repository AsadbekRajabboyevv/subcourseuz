import { Component, OnInit, OnDestroy, inject, signal, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { PageWrapperComponent } from '../../shared/ui/layout/page-wrapper.component';
import { HomeService } from './home.service';
import {
  HomePageData,
  HomeCourse,
  HomeCourseGrade,
  HomeComment,
  StatCard,
  QuickAction,
  StatKey
} from './home.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, PageWrapperComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  private readonly homeService = inject(HomeService);
  private readonly router = inject(Router);

  isLoading = signal(true);
  data = signal<HomePageData | null>(null);
  currentSlideIndex = 0;

  // Responsive: how many cards visible at once
  visibleCount = this.getVisibleCount();

  private autoSlideTimer: ReturnType<typeof setInterval> | null = null;
  private touchStartX = 0;

  get stats() { return this.data()?.stats ?? null; }
  get courseGrades(): HomeCourseGrade[] { return this.data()?.courseGrades ?? []; }
  get topCourses(): HomeCourse[] { return this.data()?.topCourses ?? []; }
  get topComments(): HomeComment[] { return this.data()?.topComments ?? []; }

  // Card width % based on visibleCount
  get cardWidthPct(): number { return 100 / this.visibleCount; }

  // Max slide index so last page is full
  get maxSlideIndex(): number {
    return Math.max(0, this.topCourses.length - this.visibleCount);
  }

  // Dot indicators
  get dotCount(): number {
    return Math.max(1, this.topCourses.length - this.visibleCount + 1);
  }

  get dotItems(): number[] {
    return Array.from({ length: this.dotCount }, (_, i) => i);
  }

  readonly prevLabel = $localize`:@@home.courses.prev:Oldingi`;
  readonly nextLabel = $localize`:@@home.courses.next:Keyingi`;

  readonly statCards: StatCard[] = [
    { key: 'coursesCount',      label: $localize`:@@home.stats.courses:Faol kurslar`,  icon: 'fa-book-open',   color: 'emerald', borderH: 'hover:border-emerald-500' },
    { key: 'usersCount',        label: $localize`:@@home.stats.users:Talabalar`,        icon: 'fa-users',       color: 'blue',    borderH: 'hover:border-blue-500'    },
    { key: 'videoCoursesCount', label: $localize`:@@home.stats.videos:Video kurslar`,   icon: 'fa-circle-play', color: 'purple',  borderH: 'hover:border-purple-500'  },
    { key: 'testsCount',        label: $localize`:@@home.stats.tests:Testlar`,          icon: 'fa-vial',        color: 'orange',  borderH: 'hover:border-orange-500'  },
  ];

  readonly skeletonItems: number[] = [1, 2, 3, 4];

  readonly quickActions: QuickAction[] = [
    {
      title:       $localize`:@@home.actions.courses.title:Kurslar`,
      description: $localize`:@@home.actions.courses.desc:Barcha kurslarni ko'ring`,
      icon: 'fa-book-open', color: 'emerald', borderH: 'hover:border-emerald-500', route: '/courses-list'
    },
    {
      title:       $localize`:@@home.actions.tests.title:Testlar`,
      description: $localize`:@@home.actions.tests.desc:Bilimingizni sinab ko'ring`,
      icon: 'fa-vial', color: 'blue', borderH: 'hover:border-blue-500', route: '/tests-list'
    },
    {
      title:       $localize`:@@home.actions.my_courses.title:Mening kurslarim`,
      description: $localize`:@@home.actions.my_courses.desc:Sotib olingan kurslar`,
      icon: 'fa-graduation-cap', color: 'purple', borderH: 'hover:border-purple-500', route: '/courses-me'
    },
  ];

  @HostListener('window:resize')
  onResize(): void {
    const prev = this.visibleCount;
    this.visibleCount = this.getVisibleCount();
    if (prev !== this.visibleCount) {
      // Clamp index after resize
      this.currentSlideIndex = Math.min(this.currentSlideIndex, this.maxSlideIndex);
    }
  }

  ngOnInit(): void {
    this.homeService.getHomePage().subscribe({
      next: res => {
        this.data.set(res.data);
        this.isLoading.set(false);
        this.startAutoSlide();
      },
      error: () => this.isLoading.set(false)
    });
  }

  ngOnDestroy(): void {
    if (this.autoSlideTimer) clearInterval(this.autoSlideTimer);
  }

  private startAutoSlide(): void {
    this.autoSlideTimer = setInterval(() => this.nextSlide(), 5000);
  }

  private getVisibleCount(): number {
    const w = typeof window !== 'undefined' ? window.innerWidth : 1024;
    if (w < 640) return 1;   // mobile
    if (w < 1024) return 2;  // tablet
    return 4;                // desktop
  }

  nextSlide(): void {
    this.currentSlideIndex = this.currentSlideIndex >= this.maxSlideIndex
      ? 0
      : this.currentSlideIndex + 1;
  }

  prevSlide(): void {
    this.currentSlideIndex = this.currentSlideIndex <= 0
      ? this.maxSlideIndex
      : this.currentSlideIndex - 1;
  }

  goToDot(index: number): void {
    this.currentSlideIndex = Math.min(index, this.maxSlideIndex);
  }

  // Touch / swipe support
  onTouchStart(e: TouchEvent): void {
    this.touchStartX = e.touches[0].clientX;
  }

  onTouchEnd(e: TouchEvent): void {
    const diff = this.touchStartX - e.changedTouches[0].clientX;
    if (Math.abs(diff) > 40) {
      diff > 0 ? this.nextSlide() : this.prevSlide();
    }
  }

  goToCourse(slug: string): void { this.router.navigate(['/courses-view', slug]); }
  goToCourses(): void { this.router.navigate(['/courses-list']); }

  getStatValue(key: StatKey): number { return this.stats?.[key] ?? 0; }

  formatPrice(price: number): string {
    if (!price) return $localize`:@@home.price.free:Bepul`;
    return new Intl.NumberFormat('uz-UZ').format(price) + ' UZS';
  }

  formatDate(dateStr: string): string {
    return new Date(dateStr).toLocaleDateString('uz-UZ', { year: 'numeric', month: 'short', day: 'numeric' });
  }

  filledStars(rating: number): number[] { return Array.from({ length: Math.round(rating) }); }
  emptyStars(rating: number): number[] { return Array.from({ length: 5 - Math.round(rating) }); }
}
