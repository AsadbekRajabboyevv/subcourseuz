import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class PageLoaderService {
  isLoading = signal(false);
  progress = signal(0);

  private progressInterval: ReturnType<typeof setInterval> | null = null;

  start(): void {
    this.isLoading.set(true);
    this.progress.set(0);
    this.simulateProgress();
  }

  complete(): void {
    if (this.progressInterval) {
      clearInterval(this.progressInterval);
      this.progressInterval = null;
    }
    this.progress.set(100);
    setTimeout(() => {
      this.isLoading.set(false);
      this.progress.set(0);
    }, 300);
  }

  private simulateProgress(): void {
    if (this.progressInterval) clearInterval(this.progressInterval);
    this.progressInterval = setInterval(() => {
      const current = this.progress();
      if (current < 85) {
        // Slow down as it approaches 85%
        const increment = current < 30 ? 8 : current < 60 ? 4 : 1;
        this.progress.set(Math.min(current + increment, 85));
      }
    }, 100);
  }
}
