import {Injectable} from '@angular/core';
import {BaseApiService} from "../../../common/services/base.service";
import {Observable} from "rxjs";
import {Base, Pageable} from "../../../common/model/base";
import {
  Course,
  CourseFilter,
  CourseGrade,
  CourseGradeRequest,
  CourseInfo,
  CourseRequest
} from "../model/course";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CourseService extends BaseApiService {
  protected override readonly path = `/v1/api/courses`;
  private readonly publicPath = `${environment.apiPath}/v1/api/public/courses`;

  getPaginated(filter: Partial<CourseFilter>, page: number = 0, size: number = 10, sort: string = 'id,desc'): Observable<Base<Pageable<Course>>> {
    const params = {...filter, page, size, sort};
    return this.get<Base<Pageable<Course>>>(params);
  }

  getPublicPaginated(filter: Partial<CourseFilter>, page: number = 0, size: number = 10, sort: string = 'id,desc'): Observable<Base<Pageable<Course>>> {
    const params = {...filter, page, size, sort};
    return this.http.get<Base<Pageable<Course>>>(this.publicPath, {params});
  }

  getCourseInfo(id: number): Observable<Base<CourseInfo>> {
    return this.getById<Base<CourseInfo>>(id);
  }

  enroll(id: number): Observable<Base<boolean>> {
    return this.http.post<Base<boolean>>(`${this.apiUrl}${this.path}/enroll/${id}`, {});
  }

  getGrades(): Observable<Base<CourseGrade[]>> {
    return this.http.get<Base<CourseGrade[]>>(`${this.apiUrl}${this.path}/grades`);
  }

  createCourse(request: CourseRequest): Observable<Base<number>> {
    return this.post<Base<number>>(request);
  }

  deleteCourse(id: number): Observable<Base<number>> {
    return this.delete<Base<number>>(id);
  }

  saveGrade(request: CourseGradeRequest): Observable<Base<CourseGrade>> {
      return this.http.post<Base<CourseGrade>>(`${this.apiUrl}${this.path}/grades`, request);
  }

  updateGrade(id: number, request: CourseGradeRequest): Observable<Base<CourseGrade>> {
    return this.http.put<Base<CourseGrade>>(`${this.apiUrl}${this.path}/grades`, request);
  }

  updateCourse(id: number, request: CourseRequest): Observable<Base<number>> {
    return this.put<Base<number>>(id, request);
  }
}
