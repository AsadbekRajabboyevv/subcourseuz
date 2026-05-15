import {Component, inject, signal, OnInit} from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  AbstractControl
} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Router, RouterLink} from '@angular/router';

import {TestCreate, TestGenerateRequestDto} from '../test.model';
import {TestService} from "../test.service";
import {ScienceService} from "../../science/science.service";
import {GradeService} from "../../grade/grade.service";
import {InputComponent} from "../../../shared/ui/forms/input.component";
import {PageWrapperComponent} from "../../../shared/ui/layout/page-wrapper.component";

@Component({
  selector: 'app-test-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, InputComponent, PageWrapperComponent],
  templateUrl: './test-create.component.html'
})
export class TestCreateComponent implements OnInit {
  private fb = inject(FormBuilder);
  private testService = inject(TestService);
  private router = inject(Router);
  private scienceService = inject(ScienceService);
  private gradeService = inject(GradeService);

  activeTab = signal<'manual' | 'ai' | 'json'>('manual');
  isLoading = signal(false);
  aiLoading = signal(false);
  jsonLoading = signal(false);
  aiError = signal<string | null>(null);
  jsonError = signal<string | null>(null);

  mainImage: File | null = null;
  mainImagePreview: string | null = null;

  scienceOptions = signal<{ label: string; value: number }[]>([]);
  gradeOptions = signal<{ label: string; value: number }[]>([]);
  langOptions = [
    {label: 'O‘zbekcha', value: 'uz'},
    {label: 'Русский', value: 'ru'},
    {label: 'English', value: 'en'}
  ];

  testForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    description: [''],
    price: [0, [Validators.required, Validators.min(0)]],
    lang: ['uz', Validators.required],
    scienceId: [null as number | null, Validators.required],
    gradeId: [null as number | null, Validators.required],
    courseId: [null as number | null],
    lessonId: [null as number | null],
    duration: [60, [Validators.required, Validators.min(5)]],
    isPublished: [true],
    questions: this.fb.array<FormGroup>([]),
    count: [30, [Validators.required, Validators.min(1)]],
    maxScore: [100, [Validators.required, Validators.min(1)]],
    enabledViewCorrectAnswers: [false, [Validators.required]]
  });

  aiForm = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    lang: ['uz'],
    count: [30, [Validators.required, Validators.min(1)]],
    isPublished: [true],
    scienceId: [null as number | null, Validators.required],
    gradeId: [null as number | null, Validators.required],
    courseId: [null as number | null],
    lessonId: [null as number | null],
    sourceFile: [null as File | null, Validators.required],
    duration: [60, [Validators.required]],
    price: [0, [Validators.required]],
    maxScore: [100, [Validators.required, Validators.min(1)]],
    enabledViewCorrectAnswers: [false, [Validators.required]]
  });

  jsonForm = this.fb.group({
    json: [''],
  });

  ngOnInit() {
    this.loadInitialData();
    this.addQuestion();
  }

  private loadInitialData() {
    this.scienceService.get().subscribe(res => {
      this.scienceOptions.set((res?.data || []).map((s: any) => ({label: s.name, value: s.id})));
    });
    this.gradeService.get().subscribe(res => {
      this.gradeOptions.set((res?.data || []).map((g: any) => ({label: g.name, value: g.id})));
    });
  }

  get questions() {
    return this.testForm.controls.questions as FormArray;
  }

  getOptions(qIdx: number) {
    return this.questions.at(qIdx).get('options') as FormArray;
  }

  getCorrectOptionControl(qIdx: number): FormControl {
    return this.questions.at(qIdx).get('correctOptionIndex') as FormControl;
  }

  addQuestion() {
    const qGroup = this.fb.group({
      text: ['', Validators.required],
      image: [null as File | null],
      imagePreview: [null as string | null],
      correctOptionIndex: [0, Validators.required],
      options: this.fb.array([this.createOption(), this.createOption(), this.createOption(), this.createOption()])
    });
    this.questions.push(qGroup);
  }

  createOption(): FormGroup {
    return this.fb.group({
      text: ['', Validators.required],
      image: [null as File | null],
      imagePreview: [null as string | null]
    });
  }

  addOption(qIdx: number) {
    this.getOptions(qIdx).push(this.createOption());
  }

  deleteOption(qIdx: number, oIdx: number) {
    const options = this.getOptions(qIdx);
    if (options.length > 2) {
      const correctCtrl = this.getCorrectOptionControl(qIdx);
      if (correctCtrl.value === oIdx) correctCtrl.setValue(0);
      else if (correctCtrl.value > oIdx) correctCtrl.setValue(correctCtrl.value - 1);
      options.removeAt(oIdx);
    }
  }

  onFileSelected(event: Event, group: AbstractControl) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      const file = input.files[0];
      group.patchValue({image: file});
      const reader = new FileReader();
      reader.onload = () => group.patchValue({imagePreview: reader.result});
      reader.readAsDataURL(file);
    }
  }

  onMainImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.mainImage = input.files[0];
      const reader = new FileReader();
      reader.onload = () => this.mainImagePreview = reader.result as string;
      reader.readAsDataURL(this.mainImage);
    }
  }

  onAiFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) this.aiForm.patchValue({sourceFile: input.files[0]});
  }

  onSubmit() {
    if (this.testForm.invalid) return this.testForm.markAllAsTouched();

    this.isLoading.set(true);
    const v = this.testForm.getRawValue();

    const payload: TestCreate = {
      name: v.name ?? '',
      price: v.price ?? 0,
      lang: v.lang ?? 'uz',
      duration: v.duration ?? 60,
      isPublished: v.isPublished ?? false,
      description: v.description ?? undefined,
      scienceId: v.scienceId ?? undefined,
      gradeId: v.gradeId ?? undefined,
      courseId: v.courseId ?? undefined,
      lessonId: v.lessonId ?? undefined,
      count: v.count ?? 30,
      maxScore: v.maxScore ?? 100,
      enabledViewCorrectAnswers: v.enabledViewCorrectAnswers ?? false,
      questions: (v.questions || []).map((q: any) => ({
        text: q.text ?? '',
        correctOptionIndex: q.correctOptionIndex ?? 0,
        image: q.image,
        options: (q.options || []).map((o: any) => ({
          text: o.text ?? '',
          image: o.image
        }))
      }))
    };

    this.testService.create(payload, this.mainImage || new File([], 'empty')).subscribe({
      next: () => this.router.navigate(['/tests-list']),
      error: (err) => {
        this.isLoading.set(false);
        console.error(err);
      }
    });
  }

  onAiSubmit() {
    if (this.aiForm.invalid) return this.aiForm.markAllAsTouched();

    this.aiLoading.set(true);
    const v = this.aiForm.getRawValue();

    const requestDto: TestGenerateRequestDto = {
      name: v.name!,
      description: v.description ?? '',
      lang: v.lang ?? 'uz',
      count: v.count!,
      isPublished: v.isPublished ?? true,
      scienceId: Number(v.scienceId),
      gradeId: Number(v.gradeId),
      courseId: v.courseId ?? undefined,
      lessonId: v.lessonId ?? undefined,
      duration: v.duration ?? 30,
      price: v.price ?? 0,
      maxScore: v.maxScore ?? 100,
      enabledViewCorrectAnswers: v.enabledViewCorrectAnswers ?? false
    };

    const file = v.sourceFile as File;

    this.testService.generateFromFile({
      file: file,
      ...requestDto
    }).subscribe({
      next: (res) => {
        this.aiLoading.set(false);
        this.router.navigate(['/tests-list']);
      },
      error: (err) => {
        this.aiLoading.set(false);
        this.aiError.set("AI generatsiya jarayonida xatolik yuz berdi!");
        console.error(err);
      }
    });
  }

  onJsonSubmit() {
    if (this.jsonForm.invalid) {
      this.jsonForm.markAllAsTouched();
      return;
    }

    this.jsonLoading.set(true);
    this.jsonError.set(null);

    try {
      const rawJson = this.jsonForm.get('json')?.value;
      const jsonValue = typeof rawJson === 'string' ? JSON.parse(rawJson) : rawJson;

      const payload = {
        mainImage: this.mainImage,
        jsonValue: jsonValue
      };

      this.testService.generateFromJson(payload).subscribe({
        next: (res) => {
          this.jsonLoading.set(false);
          this.router.navigate(['/tests-list']);
        },
        error: (err) => {
          this.jsonLoading.set(false);
          this.jsonError.set(err.error?.message || "JSON generatsiya jarayonida xatolik yuz berdi!");
          console.error(err);
        }
      });
    } catch (e) {
      this.jsonLoading.set(false);
      this.jsonError.set("JSON formati noto'g'ri!");
    }
  }
}
