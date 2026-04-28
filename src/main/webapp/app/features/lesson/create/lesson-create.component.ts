import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { InputComponent } from "../../../shared/ui/forms/input.component";
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";
import { LessonCreate } from "../lesson.model";
import { SafeUrlPipe } from "../../../common/safeurl/safe-url.pipe";
import { LessonService } from "../lesson.service";

@Component({
  selector: 'app-lesson-create',
  standalone: true,
  imports: [
    CommonModule,
    InputComponent,
    FormsModule,
    PageWrapperComponent,
    RouterLink,
    SafeUrlPipe,
  ],
  templateUrl: './lesson-create.component.html'
})
export class LessonCreateComponent implements OnInit {
  private lessonService = inject(LessonService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  isLoading = signal<boolean>(false);
  selectedFiles: File[] = [];
  slug: string | null = null;

  lessonForm: LessonCreate = {
    name: '',
    lessonNumber: 1,
    videoUrl: '',
    textContent: '',
    courseSlug: null,
    isPublished: true
  };

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.slug = this.route.snapshot.paramMap.get('slug');
      if (this.slug) {
        this.lessonForm.courseSlug = this.slug;
      }
    });
  }

  onFileSelected(event: any) {
    if (event.target.files) {
      this.selectedFiles = Array.from(event.target.files);
    }
  }

  getYoutubeId(url: string | null) {
    if (!url) return null;
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    const match = url.match(regExp);
    return (match && match[2].length === 11) ? match[2] : null;
  }

  onSubmit() {
    this.isLoading.set(true);
    const youtubeId = this.getYoutubeId(this.lessonForm.videoUrl);

    const requestDto = {
      ...this.lessonForm,
      videoUrl: youtubeId!,
      lessonNumber: Number(this.lessonForm.lessonNumber),
      courseSlug: this.lessonForm.courseSlug
    };

    this.lessonService.saveLesson(requestDto, this.selectedFiles).subscribe({
      next: (res) => {
        this.router.navigate(['/courses-view/', requestDto.courseSlug]);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Xatolik:', err);
        this.isLoading.set(false);
      }
    });
  }
}
