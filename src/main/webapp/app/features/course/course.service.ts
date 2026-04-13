import {inject, Injectable, signal} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {environment} from '../../../environments/environment';
import {Base, Page} from "app/common/model/base";
import {
  Course, CourseFilter,
  CourseGrade, CourseInfo,
  CourseScience, CourseScienceCreate, CourseScienceUpdate,
  CourseUpdate,
  OneCourseGrade,
  OneCourseScience
} from "./course.model";
import {catchError} from "rxjs/operators";

@Injectable({providedIn: 'root'})
export class CourseService {
  private readonly http = inject(HttpClient);
  private readonly PATH = `${environment.apiPath}/v1/api/courses`;
  private readonly PUBLIC_PATH = `${environment.apiPath}/v1/public`;
  private readonly SCIENCE_PATH = `${environment.apiPath}/v1/api/sciences`;

  courses = signal<Course[]>([]);
  isLoading = signal<boolean>(false);
//====================Course============================================
  get(filter: CourseFilter): Observable<Base<Page<Course>>> {
    this.isLoading.set(true);

    let params = new HttpParams();

    Object.entries(filter).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, value.toString());
      }
    });
    return this.http.get<Base<Page<Course>>>(this.PATH, {params}).pipe(
      tap((res) => {
        this.courses.set(res.data.content);
        this.isLoading.set(false);
      }),
      catchError((err) => {
        this.isLoading.set(false);
        throw err;
      })
    );
  }

  getById(id: number): Observable<Base<CourseInfo>> {
    return this.http.get<Base<CourseInfo>>(`${this.PATH}/${id}`);
  }

  create(course: Partial<Course>, image: File): Observable<Base<Course>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(course)], {type: 'application/json'}));
    formData.append('image', image);
    return this.http.post<Base<Course>>(this.PATH, formData);
  }

  update(id: number, course: Partial<CourseUpdate>, image: File): Observable<Base<Course>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(course)], {type: 'application/json'}));
    formData.append('image', image);
    return this.http.put<Base<Course>>(`${this.PATH}/${id}`, course);
  }

  delete(id: number): Observable<Base<void>> {
    return this.http.delete<Base<void>>(`${this.PATH}/${id}`).pipe(
      tap(() => {
        this.courses.update(list => list.filter(c => c.id !== id));
      })
    );
  }

  getPublic(): Observable<Base<Page<Course>>> {
    return this.http.get<Base<Page<Course>>>(`${this.PUBLIC_PATH}/courses`);
  }

//====================Grade============================================
  getGrades(): Observable<Base<CourseGrade[]>> {
    return this.http.get<Base<CourseGrade[]>>(`${this.PUBLIC_PATH}/course-grades`);
  }

  getGradeById(id: number): Observable<Base<OneCourseGrade>> {
    return this.http.get<Base<OneCourseGrade>>(`${this.PATH}/grades/${id}`);
  }

  deleteGrade(id: number): Observable<Base<void>> {
    return this.http.delete<Base<void>>(`${this.PATH}/grades/${id}`);
  }

  updateGrade(id: number, grade: Partial<CourseGrade>): Observable<Base<CourseGrade>> {
    return this.http.put<Base<CourseGrade>>(`${this.PATH}/grades/${id}`, grade);
  }

//====================Science ============================================

  getSciences(): Observable<Base<CourseScience[]>> {
    return this.http.get<Base<CourseScience[]>>(`${this.SCIENCE_PATH}`);
  }

  getScienceById(id: number): Observable<Base<OneCourseScience>> {
    return this.http.get<Base<OneCourseScience>>(`${this.SCIENCE_PATH}/${id}`);
  }

  createScience(science: CourseScienceCreate, image: File): Observable<Base<CourseScience>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(science)], {type: 'application/json'}));
    formData.append('image', image);
    return this.http.post<Base<CourseScience>>(this.SCIENCE_PATH, formData);
  }

  updateScience(id: number, science: Partial<CourseScienceUpdate>, image: File): Observable<Base<CourseScience>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(science)], {type: 'application/json'}));
    formData.append('image', image);
    return this.http.put<Base<CourseScience>>(`${this.SCIENCE_PATH}/${id}`, formData);
  }

  deleteScience(id: number): Observable<Base<void>> {
    return this.http.delete<Base<void>>(`${this.SCIENCE_PATH}/${id}`);
  }
}
