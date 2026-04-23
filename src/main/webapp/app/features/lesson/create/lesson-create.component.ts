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
    SafeUrlPipe
  ],
  templateUrl: './lesson-create.component.html'
})
export class LessonCreateComponent implements OnInit {
  private lessonService = inject(LessonService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  isLoading = signal<boolean>(false);
  selectedFiles: File[] = [];

  lessonForm: LessonCreate = {
    name: '',
    lessonNumber: 1,
    videoUrl: '',
    textContent: '',
    courseId: 0,
    isPublished: true
  };

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const cId = params['courseId'] || params['id'];
      if (cId) {
        this.lessonForm.courseId = Number(cId);
        console.log('Kurs ID yuklandi:', this.lessonForm.courseId);
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
    if (!this.lessonForm.name || !this.lessonForm.courseId) {
      alert('Iltimos, dars nomi va kurs ID mavjudligini tekshiring!');
      return;
    }

    this.isLoading.set(true);
    const youtubeId = this.getYoutubeId(this.lessonForm.videoUrl);

    const requestDto = {
      ...this.lessonForm,
      videoUrl: youtubeId!,
      lessonNumber: Number(this.lessonForm.lessonNumber),
      courseId: Number(this.lessonForm.courseId)
    };

    this.lessonService.saveLesson(requestDto, this.selectedFiles).subscribe({
      next: (res) => {
        console.log('Muvaffaqiyatli saqlandi:', res);
        this.router.navigate(['/courses-view', requestDto.courseId], {
          queryParams: { id: requestDto.courseId }
        });
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Xatolik:', err);
        this.isLoading.set(false);
      }
    });
  }
}
