import { CommonModule } from '@angular/common';
import { Component, inject, computed } from '@angular/core';
import { ThemeService } from '../services/theme.service';

@Component({
  selector: 'app-theme-switch',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './theme-switch.component.html',
})
export class ThemeSwitchComponent {
  private readonly themeService = inject(ThemeService);

  protected isDark = this.themeService.isDark;
  protected currentTheme = this.themeService.theme;

  protected toggle(): void {
    this.themeService.toggle();
  }
}

