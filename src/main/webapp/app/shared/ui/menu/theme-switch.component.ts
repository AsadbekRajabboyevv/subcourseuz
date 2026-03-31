import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ThemeService } from '../services/theme.service';

@Component({
  selector: 'app-theme-switch',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './theme-switch.component.html',
})
export class ThemeSwitchComponent {
  private readonly themeService = inject(ThemeService);

  protected toggle(): void {
    this.themeService.toggle();
  }
}

