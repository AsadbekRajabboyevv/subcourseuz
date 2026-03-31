import { Injectable } from '@angular/core';
import { TranslateLoader } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TranslationLoader implements TranslateLoader {
  constructor(private readonly http: HttpClient) {}

  getTranslation(lang: string): Observable<any> {
    return this.http.get(`/assets/i18n/${lang}.xlf`).pipe(
      catchError(() => {
        console.warn(`Translation file for ${lang} not found, falling back to uz`);
        return this.http.get('/assets/i18n/uz.xlf');
      }),
      catchError(() => {
        console.error('Failed to load any translation file');
        return of({});
      })
    );
  }
}
