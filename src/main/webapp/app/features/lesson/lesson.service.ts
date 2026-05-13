import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../environments/environment";
import {Base} from "../../common/model/base";
import {Lesson, LessonCreate, LessonInfo, LessonUpdate} from "./lesson.model";
@Injectable({
  providedIn: 'root'
})
export class LessonService {
  private readonly apiUrl = `${environment.apiPath}/v1/api/lessons`;

  isLoading = signal<boolean>(false);

  constructor(private http: HttpClient) {}


  saveLesson(request: LessonCreate, files: File[]): Observable<Base<number>> {
    const formData = new FormData();

    formData.append('request', new Blob([JSON.stringify(request)], {
      type: 'application/json'
    }));

    if (files && files.length > 0) {
      files.forEach(file => {
        formData.append('files', file);
      });
    }

    return this.http.post<Base<number>>(this.apiUrl, formData);
  }

    updateLesson(id: number, request: LessonUpdate, files: File[], deletedFileUrls: string[]): Observable<Base<number>> {
      const formData = new FormData();

      formData.append('request', new Blob([JSON.stringify(request)], {
        type: 'application/json'
      }));

      if (files && files.length > 0) {
        files.forEach(file => {
          formData.append('files', file);
        });
      }
      if (deletedFileUrls && deletedFileUrls.length > 0) {
        deletedFileUrls.forEach(url => {
          formData.append('deletedFileUrls', url);
        });
      }

      return this.http.put<Base<number>>(`${this.apiUrl}/${id}`, formData);
    }

  deleteLesson(id: number): Observable<Base<number>> {
    return this.http.delete<Base<number>>(`${this.apiUrl}/${id}`);
  }

  getLesson(id: number): Observable<Base<LessonInfo>> {
    return this.http.get<Base<any>>(`${this.apiUrl}/${id}`);
  }


  getLessonsByCourseId(courseId: number): Observable<Base<Lesson>> {
    return this.http.get<Base<Lesson>>(`${this.apiUrl}/course/${courseId}`);
  }
}
