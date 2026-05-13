import { Injectable, Inject, LOCALE_ID } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LangService {

  constructor(@Inject(LOCALE_ID) public currentLocale: string) {
    localStorage.setItem('lang', currentLocale);
  }

  setLang(lang: string) {
    if (lang === this.currentLocale) return;

    localStorage.setItem('lang', lang);
    const currentPath = window.location.pathname;
    const newPath = currentPath.replace(`/${this.currentLocale}/`, `/${lang}/`);

    if (newPath === currentPath) {
      window.location.href = `/${lang}${currentPath}`;
    } else {
      window.location.href = newPath;
    }
  }

  getLang(): string {
    return this.currentLocale;
  }
}
