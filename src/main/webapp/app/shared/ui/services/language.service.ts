import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LangService {

  private lang$ = new BehaviorSubject<string>(
    localStorage.getItem('lang') || 'uz-cyrl'
  );

  constructor(private translate: TranslateService) {
    this.translate.addLangs(['uz', 'uz-cyrl', 'ru', 'en']);
    this.translate.setDefaultLang('uz-cyrl');
    this.translate.use(this.lang$.value);
  }

  setLang(lang: string) {
    localStorage.setItem('lang', lang);
    this.translate.use(lang);
    this.lang$.next(lang);
  }

  getLang(): string {
    return this.lang$.value;
  }

  langChanges$() {
    return this.lang$.asObservable();
  }
}
