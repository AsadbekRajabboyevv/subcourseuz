import { Component, inject, signal, ViewChild, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgIconsModule, provideIcons } from '@ng-icons/core';
import {
  heroArrowPath, heroPencil, heroPlusCircle,
  heroSquares2x2, heroTrash, heroCheck, heroXCircle, heroLanguage
} from '@ng-icons/heroicons/outline';
import { finalize, switchMap } from 'rxjs';
import { CourseService } from '../service/course.service';
import {CourseGrade, CourseGradeRequest} from '../model/course';
import {DynamicFormComponent, FieldConfig, PageWrapperComponent} from "../../../shared/ui";

@Component({
  selector: 'app-course-grade',
  standalone: true,
  imports: [CommonModule, RouterModule, NgIconsModule, PageWrapperComponent, DynamicFormComponent],
  providers: [provideIcons({
    heroSquares2x2, heroArrowPath, heroPencil, heroTrash,
    heroPlusCircle, heroCheck, heroXCircle, heroLanguage
  })],
  templateUrl: './course-grade.component.html'
})
export class CourseGradeComponent implements OnInit {
  @ViewChild('gradeForm') dynamicForm!: DynamicFormComponent;

  private gradeService = inject(CourseService);

  grades = signal<CourseGrade[]>([]);
  isLoading = signal(false);
  isSubmitting = signal(false);
  isEditing = signal(false);
  selectedId = signal<number | null>(null);

  gradeFields: FieldConfig[] = [
    { key: 'nameUz', label: "Nomi (UZ)", type: 'input', required: true },
    { key: 'nameRu', label: "Название (RU)", type: 'input', required: true },
    { key: 'nameEn', label: "Name (EN)", type: 'input', required: true },
    { key: 'descriptionUz', label: "Tavsif (UZ)", type: 'textarea' },
    { key: 'descriptionRu', label: "Описание (RU)", type: 'textarea' },
    { key: 'descriptionEn', label: "Description (EN)", type: 'textarea' }
  ];

  ngOnInit() { this.loadGrades(); }

  loadGrades() {
    this.isLoading.set(true);
    this.gradeService.getGrades()
    .pipe(finalize(() => this.isLoading.set(false)))
    .subscribe(res => this.grades.set(res.data));
  }

  onEdit(id: number) {
    this.isLoading.set(true);
    this.isEditing.set(true);
    this.selectedId.set(id);

    this.gradeService.getGradeById(id)
    .pipe(finalize(() => this.isLoading.set(false)))
    .subscribe({
      next: (res) => {
        const grade = res.data;
        const flatData = {
          nameUz: grade.name?.nameUz || '',
          nameRu: grade.name?.nameRu || '',
          nameEn: grade.name?.nameEn || '',
          descriptionUz: grade.description?.descriptionUz || '',
          descriptionRu: grade.description?.descriptionRu || '',
          descriptionEn: grade.description?.descriptionEn || ''
        };

        if (this.dynamicForm?.form) {
          this.dynamicForm.form.patchValue(flatData);
        }
      },
      error: (err) => {
        console.error("Ma'lumotni yuklashda xatolik:", err);
        this.cancelEdit();
      }
    });
  }

  cancelEdit() {
    this.isEditing.set(false);
    this.selectedId.set(null);
    if (this.dynamicForm) {
      this.dynamicForm.reset();
    }
  }

  onHandleSubmit(data: Record<string, any>) {
    this.isSubmitting.set(true);

    const payload = {
      name: {
        nameUz: data['nameUz'],
        nameRu: data['nameRu'],
        nameEn: data['nameEn']
      },
      description: {
        descriptionUz: data['descriptionUz'],
        descriptionRu: data['descriptionRu'],
        descriptionEn: data['descriptionEn']
      }
    } as CourseGradeRequest;

    const request = this.isEditing() && this.selectedId()
      ? this.gradeService.updateGrade(this.selectedId()!, payload)
      : this.gradeService.saveGrade(payload);

    request.pipe(finalize(() => this.isSubmitting.set(false)))
    .subscribe(() => {
      this.loadGrades();
      this.cancelEdit();
    });
  }

  onDelete(id: number) {
    if (confirm('Oʻchirmoqchimisiz?')) {
      this.gradeService.deleteGrade(id).subscribe(() => this.loadGrades());
    }
  }
}
