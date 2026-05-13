import { Base, Page } from "../../common/model/base";
import {from, Observable} from "rxjs";
import {inject, Injectable, signal} from "@angular/core";
import {
  Test,
  TestCreate,
  TestUpdate,
  SubmitAnswer,
  TestReview,
  TestResult,
  TestSession
} from "./test.model";
import { environment } from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {AuthService} from "../../common/auth/auth.service";

@Injectable({ providedIn: 'root' })
export class TestService {
  private _tests = signal<Test[]>([]);
  public tests = this._tests.asReadonly();
  private readonly PATH = environment.apiPath + '/v1/api/tests';
  private readonly SESSION_PATH = environment.apiPath + '/v1/api/test-sessions';
  private readonly PUBLIC_PATH = environment.apiPath + '/v1/api/public/tests';
  private readonly AI_PATH = environment.apiPath + '/v1/api/ai';
  authService = inject(AuthService);
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
    let path = "";
    if (this.authService.isLoggedIn().valueOf()) {
      path = this.PATH;
    } else {
      path = this.PUBLIC_PATH;
    }
    return this.http.get<Base<Page<Test>>>(path, { params });
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

  getInfo(id: number): Observable<Base<Test>> {
    return this.http.get<Base<Test>>(`${this.PATH}/info/${id}`);
  }

  generateFromFile(payload: any): Observable<Base<number>> {
    const form = new FormData();
    form.append('file', payload.file);
    if (payload.mainImage) {
      form.append('mainImage', payload.mainImage);
    }
    const { file, mainImage, ...dtoPart } = payload;
    form.append('request', new Blob([JSON.stringify(dtoPart)], {
      type: 'application/json'
    }));

    return this.http.post<Base<number>>(`${this.AI_PATH}/test-generate`, form);
  }
//===================Session=====================================
  submitAnswer(submitData: SubmitAnswer): Observable<Base<boolean>> {
    return this.http.put<Base<boolean>>(`${this.SESSION_PATH}/submit`, submitData);
  }

  start(id: number): Observable<Base<number>> {
    return this.http.post<Base<number>>(`${this.SESSION_PATH}/start/${id}`, {});
  }

  finish(id: number): Observable<Base<TestResult>> {
    return this.http.post<Base<TestResult>>(`${this.SESSION_PATH}/finish/${id}`, {});
  }

  getReview(sessionId: number): Observable<Base<TestReview>> {
    return this.http.get<Base<TestReview>>(`${this.SESSION_PATH}/review/${sessionId}`);
  }

  getSession(sessionId: number): Observable<Base<TestSession>> {
    return this.http.get<Base<TestSession>>(`${this.SESSION_PATH}/${sessionId}`);
  }
}
