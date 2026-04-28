import { Injectable, computed, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly STORAGE_KEY = 'theme';

  // Read from localStorage immediately — index.html script already applied the class,
  // so the signal just needs to reflect the real current state.
  theme = signal<'light' | 'dark'>(this.getSavedTheme());
  isDark = computed(() => this.theme() === 'dark');

  private getSavedTheme(): 'light' | 'dark' {
    try {
      const saved = localStorage.getItem(this.STORAGE_KEY);
      return saved === 'dark' ? 'dark' : 'light';
    } catch {
      return 'light';
    }
  }

  toggle(): void {
    this.setTheme(this.theme() === 'light' ? 'dark' : 'light');
  }

  setTheme(theme: 'light' | 'dark'): void {
    this.theme.set(theme);
    try {
      localStorage.setItem(this.STORAGE_KEY, theme);
    } catch { /* ignore */ }
    this.applyTheme(theme);
  }

  private applyTheme(theme: 'light' | 'dark'): void {
    if (theme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }
}
