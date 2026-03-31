import { Injectable, computed, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly STORAGE_KEY = 'theme';

  theme = signal<'light' | 'dark'>('light');
  isDark = computed(() => this.theme() === 'dark');

  constructor() {
    const saved = localStorage.getItem(this.STORAGE_KEY) as 'light' | 'dark' | null;
    const initial = saved ?? 'light';
    this.theme.set(initial);
    this.applyTheme(initial);
  }

  toggle(): void {
    this.setTheme(this.theme() === 'light' ? 'dark' : 'light');
  }

  setTheme(theme: 'light' | 'dark'): void {
    this.theme.set(theme);
    localStorage.setItem(this.STORAGE_KEY, theme);
    this.applyTheme(theme);
  }

  applyTheme(theme: 'light' | 'dark'): void {
    if (theme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }
}
