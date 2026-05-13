import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PageLoaderService } from './page-loader.service';

@Component({
  selector: 'app-page-loader',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (loader.isLoading()) {
      <div class="page-progress-bar">
        <div class="page-progress-fill" [style.width.%]="loader.progress()"></div>
        <div class="page-progress-glow"></div>
      </div>
    }
  `,
  styles: [`
    .page-progress-bar {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      height: 7px;
      z-index: 9999;
      background: transparent;
      overflow: visible;
    }

    .page-progress-fill {
      height: 100%;
      background: linear-gradient(90deg, #10b981, #34d399, #6ee7b7);
      border-radius: 0 2px 2px 0;
      transition: width 0.2s ease;
      position: relative;
    }

    .page-progress-glow {
      position: absolute;
      top: -2px;
      right: 0;
      width: 80px;
      height: 7px;
      background: radial-gradient(ellipse at right, rgba(16,185,129,0.8) 0%, transparent 70%);
      filter: blur(2px);
    }
  `]
})
export class PageLoaderComponent {
  protected readonly loader = inject(PageLoaderService);
}
