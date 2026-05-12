import { Component, Input, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommentService } from './comment.service';
import { CommentResponse } from './comment.model';
import { AuthService } from '../../../common/auth/auth.service';

@Component({
  selector: 'app-comment-section',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './comment-section.component.html',
})
export class CommentSectionComponent implements OnInit {
  @Input() courseSlug?: string | null;
  @Input() lessonId?: number | null;
  @Input() testId?: number | null;

  private commentService = inject(CommentService);
  protected authService = inject(AuthService);

  comments = signal<CommentResponse[]>([]);
  avgRating = signal<number>(0);
  totalElements = signal(0);
  currentPage = signal(0);
  pageSize = 10;
  isLoading = signal(false);
  isSubmitting = signal(false);

  // Form
  newText = '';
  newRating = 0;
  hoverRating = 0;
  editingId: number | null = null;
  editText = '';
  editRating = 0;
  editHoverRating = 0;
  errorMsg = '';
  successMsg = '';

  totalPages = computed(() => Math.ceil(this.totalElements() / this.pageSize));
  ratingStars = [1, 2, 3, 4, 5];

  ngOnInit(): void {
    this.load();
    this.loadAvg();
  }

  load(page = 0): void {
    this.isLoading.set(true);
    this.commentService.getPage({
      courseSlug: this.courseSlug,
      lessonId: this.lessonId,
      testId: this.testId,
      page,
      size: this.pageSize
    }).subscribe({
      next: res => {
        this.comments.set(res.data.content);
        this.totalElements.set(res.data.totalElements);
        this.currentPage.set(res.data.number);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  loadAvg(): void {
    this.commentService.getAvgRating(this.courseSlug, this.lessonId, this.testId).subscribe({
      next: res => this.avgRating.set(res.data ?? 0)
    });
  }

  submit(): void {
    this.errorMsg = '';
    if (!this.newText.trim()) { this.errorMsg = 'Izoh matni bo\'sh bo\'lmasligi kerak'; return; }
    if (this.newRating < 1)   { this.errorMsg = 'Reyting tanlang'; return; }

    this.isSubmitting.set(true);
    this.commentService.create({
      text: this.newText.trim(),
      rating: this.newRating,
      courseSlug: this.courseSlug,
      lessonId: this.lessonId,
      testId: this.testId
    }).subscribe({
      next: () => {
        this.newText = '';
        this.newRating = 0;
        this.successMsg = 'Izohingiz qo\'shildi!';
        setTimeout(() => this.successMsg = '', 3000);
        this.load(0);
        this.loadAvg();
        this.isSubmitting.set(false);
      },
      error: (err) => {
        this.errorMsg = err?.error?.message ?? 'Xatolik yuz berdi';
        this.isSubmitting.set(false);
      }
    });
  }

  startEdit(c: CommentResponse): void {
    this.editingId = c.id;
    this.editText = c.text;
    this.editRating = c.rating;
  }

  cancelEdit(): void {
    this.editingId = null;
    this.editText = '';
    this.editRating = 0;
  }

  saveEdit(c: CommentResponse): void {
    if (!this.editText.trim() || this.editRating < 1) return;
    this.commentService.update(c.id, {
      text: this.editText.trim(),
      rating: this.editRating,
      courseSlug: this.courseSlug,
      lessonId: this.lessonId,
      testId: this.testId
    }).subscribe({
      next: () => {
        this.cancelEdit();
        this.load(this.currentPage());
        this.loadAvg();
      }
    });
  }

  deleteComment(id: number): void {
    if (!confirm('Izohni o\'chirishni tasdiqlaysizmi?')) return;
    this.commentService.delete(id).subscribe({
      next: () => {
        this.load(this.currentPage());
        this.loadAvg();
      }
    });
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages()) return;
    this.load(page);
  }

  isOwner(c: CommentResponse): boolean {
    return this.authService.currentUser()?.id === c.createdBy;
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  filledStars(rating: number): number[] {
    return Array.from({ length: Math.round(rating) }, (_, i) => i + 1);
  }

  emptyStars(rating: number): number[] {
    return Array.from({ length: 5 - Math.round(rating) }, (_, i) => i + 1);
  }

  formatDate(dateStr: string): string {
    return new Date(dateStr).toLocaleDateString('uz-UZ', {
      year: 'numeric', month: 'short', day: 'numeric'
    });
  }
}
