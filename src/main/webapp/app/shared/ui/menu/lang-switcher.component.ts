import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LangService } from '../services/language.service';

@Component({
  selector: 'app-lang-switcher',
  standalone: true,
  imports: [CommonModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  templateUrl: './lang-switcher.component.html',
  styles: [`
    .relative { position: relative; }
    .absolute { position: absolute; }
    .fixed { position: fixed; inset: 0; }
    .z-50 { z-index: 50; }

    button {
      cursor: pointer;
      border: none;
      background: transparent;
      display: flex;
      align-items: center;
    }
  `]
})
export class LangSwitcherComponent {

  languages = [
    { code: 'uz', name: "O'zbek", flag: '🇺🇿' },
    { code: 'uz-Cyrl', name: 'Ўзбекча', flag: '🇺🇿' },
    { code: 'ru', name: 'Русский', flag: '🇷🇺' },
    { code: 'en', name: 'English', flag: '🇺🇸' }
  ];

  currentLang = this.langService.getLang();
  showDropdown = false;

  constructor(public langService: LangService) {}

  get currentLangObj() {
    return this.languages.find(l => l.code === this.currentLang) || this.languages[0];
  }

  changeLang(code: string) {
    this.langService.setLang(code);
    this.currentLang = code;
    this.showDropdown = false;
  }
}
