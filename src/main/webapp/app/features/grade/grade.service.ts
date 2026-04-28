import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Base} from "../../common/model/base";
import {CourseGrade, OneCourseGrade} from "./grade.model";
import {Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class GradeService {
  private readonly http = inject(HttpClient);
  private readonly PATH = `${environment.apiPath}/v1/api/course-grades`;
  private readonly PUBLIC_PATH = `${environment.apiPath}/v1/api/public`;

  get(): Observable<Base<CourseGrade[]>> {
    return this.http.get<Base<CourseGrade[]>>(`${this.PUBLIC_PATH}/course-grades`);
  }

  getGradeById(id: number): Observable<Base<OneCourseGrade>> {
    return this.http.get<Base<OneCourseGrade>>(`${this.PATH}/${id}`);
  }

  deleteGrade(id: number): Observable<Base<void>> {
    return this.http.delete<Base<void>>(`${this.PATH}/${id}`);
  }

  updateGrade(id: number, grade: Partial<CourseGrade>): Observable<Base<CourseGrade>> {
    return this.http.put<Base<CourseGrade>>(`${this.PATH}/${id}`, grade);
  }
}
