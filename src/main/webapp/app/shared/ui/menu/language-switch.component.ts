import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslationService } from '../../../core/services/translation.service';

@Component({
  selector: 'app-language-switch',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './language-switch.component.html',
})
export class LanguageSwitchComponent {
  private readonly translationService = inject(TranslationService);

  isDropdownOpen = false;

  readonly languages = [
    { code: 'uz', name: 'Uzbek', flag: '🇺🇿' },
    { code: 'ru', name: 'Русский', flag: '🇷🇺' },
    { code: 'en', name: 'English', flag: '🇬🇧' },
    { code: 'uz-Cyrl', name: 'Ўзбекча', flag: '🇺🇿' }
  ];

  get currentLanguage(): string {
    return this.translationService.getCurrentLanguage();
  }

  changeLanguage(lang: string): void {
    this.translationService.changeLanguage(lang);
    this.isDropdownOpen = false;
  }

  getCurrentLanguageName(): string {
    const current = this.languages.find(lang => lang.code === this.currentLanguage);
    return current ? `${current.flag} ${current.name}` : '🌐 Language';
  }
}
