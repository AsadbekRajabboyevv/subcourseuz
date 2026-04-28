import {inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {AuthService} from "../../common/auth/auth.service";
import {Base} from "../../common/model/base";
import {Observable} from "rxjs";
import {
  CourseScience,
  CourseScienceCreate,
  CourseScienceUpdate,
  OneCourseScience
} from "./science.model";

@Injectable({providedIn: 'root'})
export class ScienceService {
  private readonly http = inject(HttpClient);
  private readonly authService = inject(AuthService);
  private readonly PATH = `${environment.apiPath}/v1/api/courses`;


  get(): Observable<Base<CourseScience[]>> {
    return this.http.get<Base<CourseScience[]>>(`${this.PATH}`);
  }

  getScienceById(id: number): Observable<Base<OneCourseScience>> {
    return this.http.get<Base<OneCourseScience>>(`${this.PATH}/${id}`);
  }

  createScience(science: CourseScienceCreate, image: File): Observable<Base<CourseScience>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(science)], {type: 'application/json'}));
    formData.append('image', image);
    return this.http.post<Base<CourseScience>>(this.PATH, formData);
  }

  updateScience(id: number, science: Partial<CourseScienceUpdate>, image: File): Observable<Base<CourseScience>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(science)], {type: 'application/json'}));
    formData.append('image', image);
    return this.http.put<Base<CourseScience>>(`${this.PATH}/${id}`, formData);
  }

  deleteScience(id: number): Observable<Base<void>> {
    return this.http.delete<Base<void>>(`${this.PATH}/${id}`);
  }
}
