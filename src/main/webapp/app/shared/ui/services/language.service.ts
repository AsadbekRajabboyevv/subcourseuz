import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LangService {

  private lang$ = new BehaviorSubject<string>(
    localStorage.getItem('lang') || 'uz-cyrl'
  );


  setLang(lang: string) {
    localStorage.setItem('lang', lang);
    this.lang$.next(lang);
  }

  getLang(): string {
    return this.lang$.value;
  }

  langChanges$() {
    return this.lang$.asObservable();
  }
}
