import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export abstract class BaseApiService {
  protected readonly http = inject(HttpClient);
  protected readonly apiUrl = environment.apiPath;

  protected abstract readonly path: string;

  /**
   * To'liq URLni xavfsiz yasash uchun yordamchi metod
   */
  private buildUrl(endpoint: string | number = ''): string {
    const subPath = endpoint ? `/${endpoint}` : '';
    return `${this.apiUrl}${this.path}${subPath}`;
  }

  protected get<T>(params: any = {}): Observable<T> {
    let httpParams = new HttpParams();
    Object.keys(params).forEach(key => {
      if (params[key] !== null && params[key] !== undefined) {
        httpParams = httpParams.append(key, params[key]);
      }
    });

    return this.http.get<T>(this.buildUrl(), { params: httpParams })
    .pipe(catchError(this.formatErrors));
  }

  protected getById<T>(id: number | string): Observable<T> {
    return this.http.get<T>(this.buildUrl(id))
    .pipe(catchError(this.formatErrors));
  }

  protected post<T>(body: object = {}): Observable<T> {
    return this.http.post<T>(this.buildUrl(), body)
    .pipe(catchError(this.formatErrors));
  }

  protected put<T>(id: number | string, body: object = {}): Observable<T> {
    return this.http.put<T>(this.buildUrl(id), body)
    .pipe(catchError(this.formatErrors));
  }

  delete<T>(id: number | string): Observable<T> {
    return this.http.delete<T>(this.buildUrl(id))
    .pipe(catchError(this.formatErrors));
  }

  private formatErrors(error: HttpErrorResponse) {
    console.error('API Error:', error);
    return throwError(() => error.error);
  }
}
