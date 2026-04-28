import { Base, Page } from "../../common/model/base";
import { Observable } from "rxjs";
import { Injectable, signal } from "@angular/core";
import { Test, TestCreate, TestUpdate, SubmitAnswer, TestReview, TestResult } from "./test.model";
import { environment } from "../../../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";

@Injectable({ providedIn: 'root' })
export class TestService {
  private _tests = signal<Test[]>([]);
  public tests = this._tests.asReadonly();
  private readonly PATH = environment.apiPath + '/v1/api/tests'

  constructor(private http: HttpClient) {}

  setTests(data: Test[]) { this._tests.set(data); }
  appendTests(data: Test[]) { this._tests.update(prev => [...prev, ...data]); }

  get(page: number, size: number, filter: any): Observable<Base<Page<Test>>> {
    return this.http.get<Base<Page<Test>>>(`${this.PATH}`, {
      params: { page, size, ...filter }
    });
  }

  getById(id: number): Observable<Base<Test>> {
    return this.http.get<Base<Test>>(`${this.PATH}/${id}`);
  }

  create(testData: TestCreate, image: File): Observable<Base<number>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(testData)], { type: 'application/json' }));
    formData.append('image', image);
    return this.http.post<Base<number>>(`${this.PATH}`, formData);
  }

  update(id: number, updateData: TestUpdate): Observable<Base<number>> {
    return this.http.patch<Base<number>>(`${this.PATH}/${id}`, updateData);
  }

  publish(id: number): Observable<Base<number>> {
    return this.http.put<Base<number>>(`${this.PATH}/publish/${id}`, {});
  }

  unpublish(id: number): Observable<Base<number>> {
    return this.http.put<Base<number>>(`${this.PATH}/unpublish/${id}`, {});
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
}
