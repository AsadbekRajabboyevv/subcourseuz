import {Component, inject, signal, ViewChild, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NgIconsModule, provideIcons} from '@ng-icons/core';
import {
  heroBeaker, heroPencil, heroTrash, heroPlusCircle,
  heroArrowPath, heroXCircle, heroPhoto, heroLanguage,
  heroCheckCircle, heroCloudArrowUp
} from '@ng-icons/heroicons/outline';
import {finalize} from 'rxjs';
import {
  ScienceResponse,
  OneScienceResponse,
  ScienceRequest,
  ScienceUpdateRequest
} from '../model/science.model';
import {ScienceService} from "../service/science.service";
import {DynamicFormComponent, FieldConfig, PageWrapperComponent} from "../../../shared/ui";

@Component({
  selector: 'app-science-manager',
  standalone: true,
  imports: [CommonModule, NgIconsModule, PageWrapperComponent, DynamicFormComponent],
  providers: [provideIcons({
    heroBeaker, heroPencil, heroTrash, heroPlusCircle,
    heroArrowPath, heroXCircle, heroPhoto, heroLanguage,
    heroCheckCircle, heroCloudArrowUp
  })],
  templateUrl: './science.component.html'
})
export class ScienceComponent implements OnInit {
  @ViewChild('scienceForm') dynamicForm!: DynamicFormComponent;
  private scienceService = inject(ScienceService);

  // Signals for state management
  sciences = signal<ScienceResponse[]>([]);
  isLoading = signal(false);
  isSubmitting = signal(false);
  isEditing = signal(false);
  selectedId = signal<number | null>(null);
  selectedFile: File | null = null;
  imagePreview: string | null = null;

  scienceFields: FieldConfig[] = [
    {key: 'nameUz', label: "Fan nomi (UZ)", type: 'input', required: true},
    {key: 'nameRu', label: "Название науки (RU)", type: 'input', required: true},
    {key: 'nameEn', label: "Science Name (EN)", type: 'input', required: true},
    {key: 'descriptionUz', label: "Tavsif (UZ)", type: 'textarea'},
    {key: 'descriptionRu', label: "Описание (RU)", type: 'textarea'},
    {key: 'descriptionEn', label: "Description (EN)", type: 'textarea'}
  ];

  ngOnInit() {
    this.loadSciences();
  }

  loadSciences() {
    this.isLoading.set(true);
    this.scienceService.getSciences()
    .pipe(finalize(() => this.isLoading.set(false)))
    .subscribe(res => this.sciences.set(res.data));
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = (e) => this.imagePreview = e.target?.result as string;
      reader.readAsDataURL(file);
    }
  }

  onEdit(id: number) {
    this.isLoading.set(true);
    this.isEditing.set(true);
    this.selectedId.set(id);

    this.scienceService.getScienceById(id)
    .pipe(finalize(() => this.isLoading.set(false)))
    .subscribe({
      next: (res) => {
        const data = res.data;
        this.imagePreview = data.imagePath;

        // Formani backenddan kelgan ko'p tilli ma'lumotlar bilan to'ldirish
        this.dynamicForm.form.patchValue({
          nameUz: data.name?.nameUz,
          nameRu: data.name?.nameRu,
          nameEn: data.name?.nameEn,
          descriptionUz: data.description?.descriptionUz,
          descriptionRu: data.description?.descriptionRu,
          descriptionEn: data.description?.descriptionEn
        });
      }
    });
  }

  onHandleSubmit(formData: Record<string, any>) {
    if (!this.selectedFile && !this.isEditing()) {
      alert("Iltimos, fan rasmini yuklang!");
      return;
    }

    this.isSubmitting.set(true);

    if (this.isEditing() && this.selectedId()) {
      const updateRequest: ScienceUpdateRequest = {
        id: this.selectedId()!,
        name: {
          nameUz: formData['nameUz'],
          nameRu: formData['nameRu'],
          nameEn: formData['nameEn']
        },
        description: {
          descriptionUz: formData['descriptionUz'],
          descriptionRu: formData['descriptionRu'],
          descriptionEn: formData['descriptionEn'],
        }
      };

      this.scienceService.updateScience(this.selectedId()!, updateRequest, this.selectedFile)
      .pipe(finalize(() => this.isSubmitting.set(false)))
      .subscribe(() => this.onSuccess());
    } else {
      const createRequest: ScienceRequest = {
        name: {
          nameUz: formData['nameUz'],
          nameRu: formData['nameRu'],
          nameEn: formData['nameEn']
        },
        description: {
          descriptionUz: formData['descriptionUz'],
          descriptionRu: formData['descriptionRu'],
          descriptionEn: formData['descriptionEn']
        }
      };

      this.scienceService.createScience(createRequest, this.selectedFile!)
      .pipe(finalize(() => this.isSubmitting.set(false)))
      .subscribe(() => this.onSuccess());
    }
  }

  private onSuccess() {
    this.loadSciences();
    this.cancelEdit();
  }

  cancelEdit() {
    this.isEditing.set(false);
    this.selectedId.set(null);
    this.selectedFile = null;
    this.imagePreview = null;
    this.dynamicForm.reset();
  }

  onDelete(id: number) {
    if (confirm('Ushbu fanni oʻchirishga aminmisiz?')) {
      this.scienceService.deleteScience(id).subscribe(() => this.loadSciences());
    }
  }
}
