import { Injectable, inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class TranslationService {
  private readonly translate = inject(TranslateService);
  
  private readonly SUPPORTED_LANGUAGES = ['en', 'ru', 'uz', 'uz-Cyrl'];
  private readonly DEFAULT_LANGUAGE = 'uz';

  constructor() {
    this.translate.addLangs(this.SUPPORTED_LANGUAGES);
    this.translate.setDefaultLang(this.DEFAULT_LANGUAGE);
    
    const browserLang = this.translate.getBrowserLang();
    const savedLang = localStorage.getItem('language') as string;
    
    const langToUse = this.SUPPORTED_LANGUAGES.includes(savedLang) 
      ? savedLang 
      : (this.SUPPORTED_LANGUAGES.includes(browserLang || '') ? browserLang! : this.DEFAULT_LANGUAGE);
    
    this.translate.use(langToUse);
    this.setDocumentLanguage(langToUse);
  }

  changeLanguage(lang: string): void {
    if (this.SUPPORTED_LANGUAGES.includes(lang)) {
      this.translate.use(lang);
      localStorage.setItem('language', lang);
      this.setDocumentLanguage(lang);
    }
  }

  getCurrentLanguage(): string {
    return this.translate.currentLang || this.DEFAULT_LANGUAGE;
  }

  private setDocumentLanguage(lang: string): void {
    document.documentElement.lang = lang;
  }
}
