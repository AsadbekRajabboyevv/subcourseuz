import { Base, Page } from "../../common/model/base";
import {from, Observable} from "rxjs";
import { Injectable, signal } from "@angular/core";
import { Test, TestCreate, TestUpdate, SubmitAnswer, TestReview, TestResult } from "./test.model";
import { environment } from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({ providedIn: 'root' })
export class TestService {
  private _tests = signal<Test[]>([]);
  public tests = this._tests.asReadonly();
  private readonly PATH = environment.apiPath + '/v1/api/tests'

  constructor(private http: HttpClient) {}

  setTests(data: Test[]) { this._tests.set(data); }
  appendTests(data: Test[]) { this._tests.update(prev => [...prev, ...data]); }

  get(page: number, size: number, filter: any): Observable<Base<Page<Test>>> {
    let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());

    if (filter) {
      Object.keys(filter).forEach(key => {
        const value = filter[key];
        if (value !== null && value !== undefined && value !== '') {
          params = params.set(key, value.toString());
        }
      });
    }

    return this.http.get<Base<Page<Test>>>(`${this.PATH}`, { params });
  }

  getById(id: number): Observable<Base<Test>> {
    return this.http.get<Base<Test>>(`${this.PATH}/${id}`);
  }

  create(testData: TestCreate, mainImage: File): Observable<Base<number>> {
    const formData = new FormData();

    const requestPayload = {
      ...testData,
      questions: testData.questions.map(q => ({
        ...q,
        image: null,
        options: q.options.map(o => ({ ...o, image: null }))
      }))
    };

    formData.append('request', new Blob([JSON.stringify(requestPayload)], { type: 'application/json' }));

    if (mainImage) {
      formData.append('mainImage', mainImage);
    }

    testData.questions.forEach((question, qIdx) => {
      if (question.image instanceof File) {
        const extension = question.image.name.split('.').pop();
        formData.append('questionImages', question.image, `q_${qIdx}.${extension}`);
      }

      question.options.forEach((option, oIdx) => {
        if (option.image instanceof File) {
          const extension = option.image.name.split('.').pop();
          formData.append('optionImages', option.image, `q_${qIdx}_opt_${oIdx}.${extension}`);
        }
      });
    });

    return this.http.post<Base<number>>(`${this.PATH}`, formData);
  }

  update(id: number, testData: TestUpdate, mainImage: File | null): Observable<Base<number>> {
    const formData = new FormData();

    const cleanRequest = {
      ...testData,
      questions: testData.questions?.map(q => ({
        ...q,
        image: null,
        options: q.options?.map(o => ({ ...o, image: null }))
      }))
    };

    formData.append('request', new Blob([JSON.stringify(cleanRequest)], { type: 'application/json' }));

    if (mainImage) {
      formData.append('image', mainImage, mainImage.name);
    }

    testData.questions?.forEach((question, qIdx) => {
      if (question.image instanceof File) {
        const extension = question.image.name.split('.').pop();
        formData.append('questionImages', question.image, `q_${qIdx}.${extension}`);
      }

      question.options?.forEach((option, oIdx) => {
        if (option.image instanceof File) {
          const extension = option.image.name.split('.').pop();
          formData.append('optionImages', option.image, `q_${qIdx}_opt_${oIdx}.${extension}`);
        }
      });
    });

    return this.http.patch<Base<number>>(`${this.PATH}/${id}`, formData);
  }

  submitAnswer(submitData: SubmitAnswer): Observable<Base<boolean>> {
    return this.http.put<Base<boolean>>(`${this.PATH}/submit`, submitData);
  }

  start(id: number): Observable<Base<number>> {
    return this.http.post<Base<number>>(`${this.PATH}/start/${id}`, {});
  }

  finish(id: number): Observable<Base<TestResult>> {
    return this.http.post<Base<TestResult>>(`${this.PATH}/finish/${id}`, {});
  }

  getReview(sessionId: number): Observable<Base<TestReview[]>> {
    return this.http.get<Base<TestReview[]>>(`${this.PATH}/review/${sessionId}`);
  }

  getInfo(id: number): Observable<Base<Test>> {
    return this.http.get<Base<Test>>(`${this.PATH}/info/${id}`);
  }

  generateFromFile(payload: {
    file: File;
    name: string;
    description?: string;
    lang?: string;
    count?: number;
    isPublished?: boolean;
    scienceId: number;
    gradeId: number;
    courseId?: number;
    lessonId?: number;
    duration?: number;
    price?: number;
  }): Observable<Base<number>> {
    const form = new FormData();
    form.append('file', payload.file);
    form.append('name', payload.name);
    if (payload.description !== undefined && payload.description !== null) form.append('description', payload.description);
    if (payload.lang) form.append('lang', payload.lang);
    if (payload.count !== undefined && payload.count !== null) form.append('questionCount', String(payload.count));
    if (payload.isPublished !== undefined && payload.isPublished !== null) form.append('isPublished', String(payload.isPublished));
    form.append('scienceId', String(payload.scienceId));
    form.append('gradeId', String(payload.gradeId));
    form.append('duration', String(payload.duration));
    if (payload.courseId !== undefined && payload.courseId !== null) form.append('courseId', String(payload.courseId));
    if (payload.lessonId !== undefined && payload.lessonId !== null) form.append('lessonId', String(payload.lessonId));

    return this.http.post<Base<number>>(`${this.PATH}/ai-generate`, form);
  }
}
