import {Component, inject, OnInit, signal} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import { CourseService } from '../course.service';
import { PageWrapperComponent } from '../../../shared/ui/layout/page-wrapper.component';
import { InputComponent } from '../../../shared/ui/forms/input.component';
import {GradeService} from "../../grade/grade.service";
import {ScienceService} from "../../science/science.service";

@Component({
  selector: 'app-course-create',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    PageWrapperComponent,
    InputComponent,
    RouterLink
  ],
  templateUrl: './course-create.component.html'
})
export class CourseCreateComponent implements OnInit {

  private courseService = inject(CourseService);
  private gradeService = inject(GradeService);
  private scienceService = inject(ScienceService);
  private router = inject(Router);

  isLoading = signal(false);
  selectedImage: File | null = null;

  courseForm = {
    name: '',
    description: '',
    duration: null,
    durationType: 'SOAT',
    scienceId: null,
    gradeId: null,
    price: 0,
    lang: 'UZ',
    isVideoCourse: true,
    isPublished: true
  };

  durationTypes = [
    { label: 'Soat', value: 'SOAT' },
    { label: 'Yil', value: 'YIL' },
    { label: 'Oy', value: 'OY' }
  ];

  languages = [
    { label: 'O\'zbekcha', value: 'UZ' },
    { label: 'Ruscha', value: 'RU' },
    { label: 'Inglizcha', value: 'EN' }
  ];

  sciences = signal<{ label: string, value: any }[]>([]);
  grades = signal<{ label: string, value: any }[]>([]);

  ngOnInit() {
    this.loadInitialData();
  }

  loadInitialData() {
    this.scienceService.get().subscribe({
      next: (data) => {
        this.sciences.set(data.data.map((item: any) => ({
          label: item.name,
          value: item.id
        })));
      }
    });

    this.gradeService.get().subscribe({
      next: (data) => {
        this.grades.set(data.data.map((item: any) => ({
          label: item.name,
          value: item.id
        })));
      }
    });
  }
  imagePreview: string | null = null;

  onImageSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
      const reader = new FileReader();
      reader.onload = () => this.imagePreview = reader.result as string;
      reader.readAsDataURL(file);
    }
  }
  onSubmit() {
    if (!this.selectedImage) {
      return;
    }

    if (!this.courseForm.description || this.courseForm.description.trim() === '') {
      return;
    }
    this.isLoading.set(true);

    this.courseService.create(this.courseForm as any, this.selectedImage).subscribe({
      next: (res) => {
        this.router.navigate(['/courses-list']);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Xatolik:', err);
        this.isLoading.set(false);
      }
    });
  }
}
