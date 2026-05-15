import { Component, inject, signal, OnInit } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormControl,
  AbstractControl
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TestService } from "../test.service";
import { ScienceService } from "../../science/science.service";
import { GradeService } from "../../grade/grade.service";
import { TestUpdate, QuestionUpdate, OptionUpdate } from '../test.model';
import { InputComponent } from "../../../shared/ui/forms/input.component";
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";

@Component({
  selector: 'app-test-update',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, InputComponent, PageWrapperComponent],
  templateUrl: './test-update.component.html'
})
export class TestUpdateComponent implements OnInit {
  private fb = inject(FormBuilder);
  private testService = inject(TestService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private scienceService = inject(ScienceService);
  private gradeService = inject(GradeService);

  testId: number | null = null;
  isLoading = signal(false);
  isSaving = signal(false);
  testName: string | null = null;
  mainImage: File | null = null;
  mainImagePreview: string | null = null;

  scienceOptions = signal<{ label: string; value: number }[]>([]);
  gradeOptions = signal<{ label: string; value: number }[]>([]);
  langOptions = [
    { label: 'O‘zbekcha', value: 'uz' },
    { label: 'Русский', value: 'ru' },
    { label: 'English', value: 'en' }
  ];

  updateForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    description: [''],
    price: [0, [Validators.required]],
    lang: ['uz', Validators.required],
    scienceId: [null as number | null, Validators.required],
    gradeId: [null as number | null, Validators.required],
    duration: [30, [Validators.required]],
    isPublished: [true],
    count: [0, [Validators.required, Validators.min(1)]],
    enabledViewCorrectAnswers: [false, [Validators.required]],
    maxScore: [100, [Validators.required, Validators.min(1)]],
    questions: this.fb.array<FormGroup>([])
  });

  ngOnInit() {
    this.testId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadInitialData();
    if (this.testId) {
      this.loadTestData(this.testId);
    }
  }

  private loadInitialData() {
    this.scienceService.get().subscribe(res =>
      this.scienceOptions.set((res?.data || []).map((s: any) => ({ label: s.name, value: s.id })))
    );
    this.gradeService.get().subscribe(res =>
      this.gradeOptions.set((res?.data || []).map((g: any) => ({ label: g.name, value: g.id })))
    );
  }

  private loadTestData(id: number) {
    this.isLoading.set(true);
    this.testService.getById(id).subscribe({
      next: (res) => {
        const test = res.data;
        this.testName = test.name;
        this.updateForm.patchValue({
          name: test.name,
          description: test.description,
          price: test.price,
          lang: test.lang,
          scienceId: test.scienceId,
          gradeId: test.gradeId,
          duration: test.duration,
          isPublished: test.isPublished,
          count: test.count,
          enabledViewCorrectAnswers: test.enabledViewCorrectAnswers,
          maxScore: test.maxScore
        });

        if (test.imagePath) this.mainImagePreview = test.imagePath;

        this.questions.clear();
        test.questions.forEach(q => this.addQuestion(q));

        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  get questions() {
    return this.updateForm.controls.questions as FormArray;
  }

  getOptions(qIdx: number) {
    return this.questions.at(qIdx).get('options') as FormArray;
  }

  getCorrectOptionControl(qIdx: number) {
    return this.questions.at(qIdx).get('correctOptionIndex') as FormControl;
  }

  asFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }

  addQuestion(qData?: any) {
    const qGroup = this.fb.group({
      id: [qData?.id || null],
      text: [qData?.text || '', Validators.required],
      image: [null as File | null],
      imagePreview: [qData?.imagePath || null],
      correctOptionIndex: [qData?.correctOptionIndex || 0, Validators.required],
      options: this.fb.array([])
    });

    if (qData?.options) {
      qData.options.forEach((o: any) => this.addOption(qGroup, o));
    } else {
      for (let i = 0; i < 4; i++) this.addOption(qGroup);
    }
    this.questions.push(qGroup);
  }

  addOption(control: AbstractControl, oData?: any) {
    const qGroup = control as FormGroup;
    const options = qGroup.get('options') as FormArray;

    options.push(this.fb.group({
      id: [oData?.id || null],
      text: [oData?.text || '', Validators.required],
      image: [null as File | null],
      imagePreview: [oData?.imageUrl || null]
    }));
  }

  onFileSelected(event: any, group: any) {
    const file = event.target.files[0];
    if (file) {
      group.patchValue({ image: file });
      const reader = new FileReader();
      reader.onload = () => group.patchValue({ imagePreview: reader.result });
      reader.readAsDataURL(file);
    }
  }

  onMainImageSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.mainImage = file;
      const reader = new FileReader();
      reader.onload = () => this.mainImagePreview = reader.result as string;
      reader.readAsDataURL(file);
    }
  }

  deleteQuestion(i: number) {
    this.questions.removeAt(i);
  }

  deleteOption(qIdx: number, oIdx: number) {
    const options = this.getOptions(qIdx);
    if (options.length > 2) {
      options.removeAt(oIdx);
      const correctIdx = this.getCorrectOptionControl(qIdx);
      if (correctIdx.value >= options.length) {
        correctIdx.setValue(0);
      }
    }
  }

  onSubmit() {
    if (this.updateForm.invalid || !this.testId) {
      this.updateForm.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    const v = this.updateForm.getRawValue();

    const payload: TestUpdate = {
      name: v.name ?? '',
      description: v.description ?? undefined,
      price: v.price ?? 0,
      lang: v.lang ?? 'uz',
      duration: v.duration ?? 30,
      isPublished: v.isPublished ?? false,
      scienceId: v.scienceId ? Number(v.scienceId) : undefined,
      gradeId: v.gradeId ? Number(v.gradeId) : undefined,
      count: v.count ?? 0,
      maxScore: v.maxScore ?? 100,
      enabledViewCorrectAnswers: v.enabledViewCorrectAnswers ?? false,
      questions: (v.questions || []).map((q: any): QuestionUpdate => ({
        id: q.id ?? undefined,
        text: q.text,
        correctOptionIndex: q.correctOptionIndex,
        image: q.image,
        options: q.options.map((o: any): OptionUpdate => ({
          id: o.id ?? undefined,
          text: o.text,
          image: o.image
        }))
      }))
    };

    this.testService.update(this.testId, payload, this.mainImage).subscribe({
      next: () => {
        this.isSaving.set(false);
        this.router.navigate(['/tests-list']);
      },
      error: (err) => {
        this.isSaving.set(false);
        console.error('Update xatolik:', err);
      }
    });
  }
}
