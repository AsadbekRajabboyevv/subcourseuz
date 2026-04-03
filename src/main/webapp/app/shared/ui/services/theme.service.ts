import { Injectable, computed, signal, EventEmitter } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly STORAGE_KEY = 'theme';

  theme = signal<'light' | 'dark'>('light');
  isDark = computed(() => this.theme() === 'dark');

  // Theme change event
  themeChange = new EventEmitter<'light' | 'dark'>();

  constructor() {
    const saved = localStorage.getItem(this.STORAGE_KEY) as 'light' | 'dark' | null;
    const initial = saved ?? 'light';
    this.theme.set(initial);
    this.applyTheme(initial);
  }

  toggle(): void {
    const newTheme = this.theme() === 'light' ? 'dark' : 'light';
    this.setTheme(newTheme);
  }

  setTheme(theme: 'light' | 'dark'): void {
    this.theme.set(theme);
    localStorage.setItem(this.STORAGE_KEY, theme);
    this.applyTheme(theme);
    this.themeChange.emit(theme);
  }

  applyTheme(theme: 'light' | 'dark'): void {
    const html = document.documentElement;
    if (theme === 'dark') {
      html.classList.add('dark');
    } else {
      html.classList.remove('dark');
    }
  }

  // Initialize theme on app start
  initializeTheme(): void {
    const saved = localStorage.getItem(this.STORAGE_KEY) as 'light' | 'dark' | null;
    const theme = saved ?? 'light';
    this.setTheme(theme);
  }
}
