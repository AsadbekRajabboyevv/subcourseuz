import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MarkdownModule } from 'ngx-markdown';
import { LessonInfo } from '../lesson.model';
import { LessonService } from '../lesson.service';
import { PageWrapperComponent } from '../../../shared/ui/layout/page-wrapper.component';
import { AuthService } from '../../../common/auth/auth.service';

@Component({
  selector: 'app-lesson-view',
  standalone: true,
  imports: [CommonModule, RouterModule, PageWrapperComponent, MarkdownModule],
  templateUrl: './lesson-view.component.html',
})
export class LessonViewComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private lessonService = inject(LessonService);
  protected authService = inject(AuthService);

  lesson = signal<LessonInfo | null>(null);
  isLoading = signal(true);

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const lessonId = params['lId'];
      if (lessonId) {
        this.lessonService.getLesson(+lessonId).subscribe({
          next: res => { this.lesson.set(res.data); this.isLoading.set(false); },
          error: () => this.isLoading.set(false)
        });
      }
    });
  }

  getYoutubeId(url: string | null | undefined): string | null {
    if (!url) return null;
    const match = url.match(/(?:v=|youtu\.be\/|embed\/)([a-zA-Z0-9_-]{11})/);
    return match ? match[1] : null;
  }
}
