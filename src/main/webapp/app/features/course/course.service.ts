import {inject, Injectable, signal} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {environment} from '../../../environments/environment';
import {Base, Page} from "app/common/model/base";
import {
  Course,
  CourseFilter,
  CourseInfo,
  CourseUpdate
} from "./course.model";
import {catchError} from "rxjs/operators";
import {AuthService} from "../../common/auth/auth.service";

@Injectable({providedIn: 'root'})
export class CourseService {
  private readonly http = inject(HttpClient);
  private readonly authService = inject(AuthService);
  private readonly PATH = `${environment.apiPath}/v1/api/courses`;
  private readonly PUBLIC_PATH = `${environment.apiPath}/v1/api/public`;

  courses = signal<Course[]>([]);
  courseLength = signal(0);
  isLoading = signal<boolean>(false);

  get(filter: CourseFilter, page: number, size: number): Observable<Base<Page<Course>>> {
    const path = !this.authService.isLoggedIn() ? `${this.PUBLIC_PATH}/courses` : this.PATH;
    return this.fetchCourses(path, filter, page, size);
  }

  getMe(filter: CourseFilter, page: number, size: number): Observable<Base<Page<Course>>> {
    return this.fetchCourses(`${this.PATH}/me`, filter, page, size);
  }

  private fetchCourses(url: string, filter: CourseFilter, page: number, size: number): Observable<Base<Page<Course>>> {
    this.isLoading.set(true);

    let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());

    Object.entries(filter).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, value.toString());
      }
    });

    return this.http.get<Base<Page<Course>>>(url, {params}).pipe(
      tap(() => this.isLoading.set(false)),
      catchError((err) => {
        this.isLoading.set(false);
        throw err;
      })
    );
  }

  setCourses(newCourses: Course[]) {
    this.courses.set(newCourses);
  }

  setCourseLength(length: number) {
    this.courseLength.set(length);
  }

  appendCourses(newCourses: Course[]) {
    this.courses.update(prev => [...prev, ...newCourses]);
  }

  getById(slug: string): Observable<Base<CourseInfo>> {
    const path = !this.authService.isLoggedIn() ? `${this.PUBLIC_PATH}/courses` : this.PATH;
    return this.http.get<Base<CourseInfo>>(`${path}/${slug}`);
  }

  create(course: Partial<Course>, image: File): Observable<Base<Course>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(course)], {type: 'application/json'}));
    formData.append('image', image);
    return this.http.post<Base<Course>>(this.PATH, formData);
  }

  update(slug: string, course: Partial<CourseUpdate>, image?: File | null): Observable<Base<Course>> {
    const formData = new FormData();

    if (course) {
      formData.append('request', new Blob([JSON.stringify(course)], {type: 'application/json'}));
    }

    if (image) {
      formData.append('image', image);
    }

    return this.http.put<Base<Course>>(`${this.PATH}/${slug}`, formData);
  }

  getUpdateBySlug(slug: string): Observable<Base<CourseUpdate>> {
    return this.http.get<Base<CourseUpdate>>(`${this.PATH}/update/${slug}`);
  }

  delete(slug: string): Observable<Base<void>> {
    return this.http.delete<Base<void>>(`${this.PATH}/${slug}`).pipe(
      tap(() => {
        this.courses.update(list => list.filter(c => c.slug !== slug));
      })
    );
  }

}
