import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { CourseService } from '../course.service';
import { CourseUpdate } from '../course.model';
import { InputComponent } from '../../../shared/ui/forms/input.component';
import { PageWrapperComponent } from '../../../shared/ui/layout/page-wrapper.component';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-course-update',
  standalone: true,
  imports: [CommonModule, FormsModule, InputComponent, PageWrapperComponent, RouterLink, TranslateModule],
  templateUrl: './course-update.component.html'
})
export class CourseUpdateComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private courseService = inject(CourseService);

  courseId!: number;
  loading = signal(false);

  selectedFile: File | null = null;
  imagePreview: string | null = null;

  sciences = signal<{ label: string, value: any }[]>([]);
  grades = signal<{ label: string, value: any }[]>([]);

  courseForm: CourseUpdate = {
    name: null,
    description: null,
    duration: null,
    durationType: null,
    scienceId: null,
    gradeId: null,
    price: null,
    lang: null,
    isVideoCourse: null,
    isPublished: null
  };

  languages = [
    { label: "O'zbekcha", value: 'UZ' },
    { label: "Ruscha", value: 'RU' },
    { label: "Inglizcha", value: 'EN' }
  ];

  durationTypes = [
    { label: 'Soat', value: 'SOAT' },
    { label: 'Kun', value: 'KUN' },
    { label: 'Oy', value: 'OY' }
  ];

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.courseId = params['id'];
    });
    this.loadInitialData();
    this.loadCourse();
  }

  loadInitialData() {
    this.courseService.getSciences().subscribe(res => {
      this.sciences.set(res.data.map((i: any) => ({ label: i.name, value: i.id })));
    });
    this.courseService.getGrades().subscribe(res => {
      this.grades.set(res.data.map((i: any) => ({ label: i.name, value: i.id })));
    });
  }

  loadCourse() {
    this.loading.set(true);
    this.courseService.getUpdateById(this.courseId).subscribe({
      next: (res: any) => {
        const data = res.data;
        this.courseForm = {
          name: data.name,
          description: data.description,
          duration: data.duration,
          durationType: data.durationType,
          scienceId: data.scienceId,
          gradeId: data.gradeId,
          price: data.price,
          lang: data.lang,
          isVideoCourse: data.isVideoCourse,
          isPublished: data.isPublished
        };
        this.imagePreview = data.imagePath;
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => this.imagePreview = reader.result as string;
      reader.readAsDataURL(file);
    }
  }

  onSubmit() {
    if (this.loading()) return;
    this.loading.set(true);
    this.courseService.update(this.courseId, this.courseForm, this.selectedFile).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/courses-list']);
      },
      error: (err) => {
        console.error("Update error:", err);
        this.loading.set(false);
      }
    });
  }
}
