import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { CommentRequest, CommentResponse } from './comment.model';

interface Base<T> { data: T; success: boolean; }
interface PageResponse<T> { content: T[]; totalElements: number; totalPages: number; number: number; }

@Injectable({ providedIn: 'root' })
export class CommentService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiPath}/v1/api/comments`;

  getPage(params: {
    courseSlug?: string | null;
    lessonId?: number | null;
    testId?: number | null;
    page?: number;
    size?: number;
  }): Observable<Base<PageResponse<CommentResponse>>> {
    let p = new HttpParams().set('page', params.page ?? 0).set('size', params.size ?? 10);
    if (params.courseSlug) p = p.set('courseSlug', params.courseSlug);
    if (params.lessonId) p = p.set('lessonId', params.lessonId);
    if (params.testId)   p = p.set('testId', params.testId);
    return this.http.get<Base<PageResponse<CommentResponse>>>(this.url, { params: p });
  }

  getAvgRating(courseSlug?: string | null, lessonId?: number | null, testId?: number | null): Observable<Base<number>> {
    let p = new HttpParams();
    if (courseSlug) p = p.set('courseSlug', courseSlug);
    if (lessonId) p = p.set('lessonId', lessonId);
    if (testId)   p = p.set('testId', testId);
    return this.http.get<Base<number>>(`${this.url}/avg-rating`, { params: p });
  }

  create(dto: CommentRequest): Observable<Base<number>> {
    return this.http.post<Base<number>>(this.url, dto);
  }

  update(id: number, dto: CommentRequest): Observable<Base<number>> {
    return this.http.put<Base<number>>(`${this.url}/${id}`, dto);
  }

  delete(id: number): Observable<Base<number>> {
    return this.http.delete<Base<number>>(`${this.url}/${id}`);
  }
}
