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
  imports: [CommonModule, InputComponent, FormsModule, PageWrapperComponent, SafeUrlPipe],
  templateUrl: './lesson-update.component.html'
})
export class LessonUpdateComponent implements OnInit {
  private lessonService = inject(LessonService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  lessonId: number | null = null;
  isLoading = signal<boolean>(false);
  isDataLoaded = signal<boolean>(false);

  // Fayllar uchun alohida saqlagichlar
  selectedFiles: File[] = [];
  deletedFileUrls: string[] = [];
  existingFileUrls: string[] = []; // UI'da ko'rsatish uchun

  // Preview ma'lumotlari
  coursePreview = { name: '', imagePath: '' };

  // Faqat LessonUpdate modeliga mos maydonlar
  lessonForm: LessonUpdate = {
    name: '',
    lessonNumber: '',
    videoUrl: '',
    textContent: '',
    courseId: null,
    isPublished: true
  };

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
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
        // Backenddan LessonInfo formatida keladi
        const data: LessonInfo = res.data ? res.data : res;

        // 1. Formaga faqat kerakli qismlarni o'zlashtiramiz
        this.lessonForm = {
          name: data.name,
          lessonNumber: data.lessonNumber?.toString(),
          videoUrl: data.videoUrl,
          textContent: data.textContent,
          courseId: data.courseId,
          isPublished: data.isPublished
        };

        // 2. Fayllarni alohida massivga olamiz
        this.existingFileUrls = data.fileUrls || [];

        // 3. Preview uchun
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

    // request (LessonUpdate), files (File[]), deletedFileUrls (string[])
    this.lessonService.updateLesson(
      this.lessonId,
      this.lessonForm,
      this.selectedFiles,
      this.deletedFileUrls
    ).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/courses-list']);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
