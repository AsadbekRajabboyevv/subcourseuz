import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { InputComponent } from "../../../shared/ui/forms/input.component";
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";
import { LessonUpdate, LessonInfo } from "../lesson.model";
import { SafeUrlPipe } from "../../../common/safeurl/safe-url.pipe";
import { LessonService } from "../lesson.service";

@Component({
  selector: 'app-lesson-update',
  standalone: true,
  imports: [
    CommonModule,
    InputComponent,
    FormsModule,
    PageWrapperComponent,
    SafeUrlPipe
  ],
  templateUrl: './lesson-update.component.html'
})
export class LessonUpdateComponent implements OnInit {
  private lessonService = inject(LessonService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private static readonly YOUTUBE_LINK_PREFIX = 'https://www.youtube.com/watch?v=';
  lessonId: number | null = null;
  isLoading = signal<boolean>(false);
  isDataLoaded = signal<boolean>(false);

  selectedFiles: File[] = [];
  deletedFileUrls: string[] = [];
  existingFileUrls: string[] = [];
  coursePreview = {
    name: '',
    imagePath: '',
    slug: ''
  };

  lessonForm: LessonUpdate = {
    name: '',
    lessonNumber: '',
    videoUrl: '',
    textContent: '',
    isPublished: true
  };

  ngOnInit() {
    this.coursePreview.slug = this.route.snapshot.paramMap.get('courseSlug') ?? '';    this.route.queryParams.subscribe(params => {
      const id = params['id'] || params['lessonId'];
      if (id) {
        this.lessonId = Number(id);
        this.fetchLessonData(this.lessonId);
      }
    });
  }

  fetchLessonData(id: number) {
    this.isLoading.set(true);
    this.lessonService.getLesson(id).subscribe({
      next: (res: any) => {
        const data: LessonInfo = res.data ? res.data : res;
        this.lessonForm = {
          name: data.name,
          lessonNumber: data.lessonNumber?.toString(),
          videoUrl: LessonUpdateComponent.YOUTUBE_LINK_PREFIX + data.videoUrl,
          textContent: data.textContent,
          isPublished: data.isPublished
        };

        this.existingFileUrls = data.fileUrls || [];

        this.coursePreview.name = data.courseName;
        this.coursePreview.imagePath = data.courseImagePath;

        this.isDataLoaded.set(true);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  removeFile(url: string) {
    this.deletedFileUrls.push(url);
    this.existingFileUrls = this.existingFileUrls.filter(f => f !== url);
  }

  onFileSelected(event: any) {
    if (event.target.files) {
      this.selectedFiles = Array.from(event.target.files);
    }
  }

  getYoutubeId(url: string | null | undefined) {
    if (!url) return null;
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    const match = url.match(regExp);
    return (match && match[2].length === 11) ? match[2] : null;
  }

  onSubmit() {
    if (!this.lessonId) return;
    this.isLoading.set(true);
    this.lessonForm.videoUrl = this.getYoutubeId(this.lessonForm.videoUrl)!;
    this.lessonService.updateLesson(
      this.lessonId,
      this.lessonForm,
      this.selectedFiles,
      this.deletedFileUrls
    ).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/courses-view/', this.coursePreview.slug]);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
