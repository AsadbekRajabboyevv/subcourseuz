import { Component, inject, signal, ViewChild, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgIconsModule, provideIcons } from '@ng-icons/core';
import { heroArrowPath, heroPencil, heroPlusCircle, heroSquares2x2, heroTrash } from '@ng-icons/heroicons/outline';
import { finalize } from 'rxjs';
import {CourseService} from "../service/course.service";
import {DynamicFormComponent, FieldConfig, PageWrapperComponent} from "../../../shared/ui";
import {CourseGrade} from "../model/course";


@Component({
  selector: 'app-course-grade-manager',
  standalone: true,
  imports: [CommonModule, RouterModule, NgIconsModule, PageWrapperComponent, DynamicFormComponent],
  providers: [provideIcons({ heroSquares2x2, heroArrowPath, heroPencil, heroTrash, heroPlusCircle })],
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

  gradeFields = GRADE_FIELDS;

  ngOnInit() {
    this.loadGrades();
  }

  loadGrades() {
    this.isLoading.set(true);
    this.gradeService.getGrades()
    .pipe(finalize(() => this.isLoading.set(false)))
    .subscribe(res => this.grades.set(res.data));
  }

  // Tahrirlash rejimiga o'tish
  onEdit(grade: any) {
    this.isEditing.set(true);
    this.selectedId.set(grade.id);

    // Formaga eski qiymatlarni yuklash (Backenddan kelgan formatni flat qilib uzatamiz)
    setTimeout(() => {
      this.dynamicForm.form.patchValue({
        nameUz: grade.nameUz,
        nameRu: grade.nameRu,
        nameEn: grade.nameEn,
        descriptionUz: grade.descriptionUz,
        descriptionRu: grade.descriptionRu,
        descriptionEn: grade.descriptionEn
      });
    });
  }

  onDelete(id: number) {
    if (confirm('Ushbu darajani oʻchirishga aminmisiz?')) {
      this.gradeService.deleteCourse(id).subscribe(() => this.loadGrades());
    }
  }

  cancelEdit() {
    this.isEditing.set(false);
    this.selectedId.set(null);
    this.dynamicForm.reset();
  }

  onHandleSubmit(data: Record<string, any>) {
    this.isSubmitting.set(true);

    const payload = {
      name: { nameUz: data['nameUz'], nameRu: data['nameRu'], nameEn: data['nameEn'], nameCrl: '' },
      description: { descriptionUz: data['descriptionUz'], descriptionRu: data['descriptionRu'], descriptionEn: data['descriptionEn'], descriptionCrl: '' }
    };

    const request = this.isEditing()
      ? this.gradeService.updateGrade(this.selectedId()!, payload)
      : this.gradeService.saveGrade(payload);

    request.pipe(finalize(() => this.isSubmitting.set(false)))
    .subscribe({
      next: () => {
        this.loadGrades();
        this.cancelEdit();
      }
    });
  }
}
export const GRADE_FIELDS: FieldConfig[] = [
  {
    key: 'nameUz',
    label: "Daraja nomi (O'zbekcha)",
    type: 'input',
    required: true,
    placeholder: "Masalan: Boshlang'ich",
    validators: [{ type: 'minLength', value: 2, message: 'Kamida 2 ta belgi' }]
  },
  {
    key: 'nameRu',
    label: "Название уровня (Русский)",
    type: 'input',
    required: true,
    placeholder: "Например: Начальный",
    validators: [{ type: 'minLength', value: 2, message: 'Минимум 2 символа' }]
  },
  {
    key: 'nameEn',
    label: "Grade Name (English)",
    type: 'input',
    required: true,
    placeholder: "Example: Beginner",
    validators: [{ type: 'minLength', value: 2, message: 'At least 2 characters' }]
  },
  {
    key: 'descriptionUz',
    label: "Tavsif (O'zbekcha)",
    type: 'textarea',
    placeholder: "Daraja haqida ma'lumot...",
  },
  {
    key: 'descriptionRu',
    label: "Описание (Русский)",
    type: 'textarea',
    placeholder: "Информация hakkida...",
  },
  {
    key: 'descriptionEn',
    label: "Description (English)",
    type: 'textarea',

    placeholder: "Details about grade...",
  }
];
