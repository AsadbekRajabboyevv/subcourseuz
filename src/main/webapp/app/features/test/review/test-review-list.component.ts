import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TestService } from '../test.service';
import { UserTestSession } from '../test.model';
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";

@Component({
  selector: 'app-test-review-list',
  standalone: true,
  imports: [CommonModule, RouterLink, PageWrapperComponent],
  templateUrl: './test-review-list.component.html'
})
export class TestReviewListComponent implements OnInit {
  private testService = inject(TestService);

  sessions = signal<UserTestSession[]>([]);
  isLoading = signal(false);

  currentPage = signal(0);
  totalPages = signal(0);
  totalElements = signal(0);
  pageSize = 10;

  ngOnInit() {
    this.loadSessions();
  }

  loadSessions(page: number = 0) {
    this.isLoading.set(true);
    this.currentPage.set(page);

    this.testService.getUserSessions(page, this.pageSize).subscribe({
      next: (res) => {
        const pageData = res.data;
        this.sessions.set(pageData.content);
        this.totalPages.set(pageData.totalPages);
        this.totalElements.set(pageData.totalElements);
        this.isLoading.set(false);
        window.scrollTo({ top: 0, behavior: 'smooth' });
      },
      error: (err) => {
        console.error('Sessiyalarni yuklashda xatolik:', err);
        this.isLoading.set(false);
      }
    });
  }

  getStatusClass(status: string): string {
    const classes: Record<string, string> = {
      'FINISHED': 'bg-emerald-50 text-emerald-600 dark:bg-emerald-500/10 dark:text-emerald-400',
      'STARTED': 'bg-blue-50 text-blue-600 dark:bg-blue-500/10 dark:text-blue-400',
      'TIME_OUT': 'bg-red-50 text-red-600 dark:bg-red-500/10 dark:text-red-400'
    };
    return classes[status] || 'bg-gray-50 text-gray-600 dark:bg-gray-800 dark:text-gray-400';
  }
}
