import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgOptimizedImage } from "@angular/common";
import { AuthService } from "../auth/auth.service";
import { ThemeSwitchComponent } from "../../shared/ui";
import { NgIconsModule, provideIcons } from '@ng-icons/core';
import {
  heroAcademicCap,
  heroArrowLeftOnRectangle,
  heroArrowRightOnRectangle,
  heroBars3,
  heroBookOpen,
  heroClipboardDocumentCheck,
  heroCog6Tooth,
  heroNewspaper,
  heroUser,
  heroViewColumns,
  heroXMark
} from '@ng-icons/heroicons/outline';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage, ThemeSwitchComponent, RouterModule, NgIconsModule],
  providers: [provideIcons({
    heroAcademicCap,
    heroUser,
    heroCog6Tooth,
    heroArrowLeftOnRectangle,
    heroArrowRightOnRectangle,
    heroXMark,
    heroBars3,
    heroBookOpen,
    heroViewColumns,
    heroClipboardDocumentCheck,
    heroNewspaper
  })],
  templateUrl: './app.header.component.html'
})
export class HeaderComponent {
  protected readonly authService = inject(AuthService);

  @Input() appTitle: string = 'Subcourse';

  isMenuOpen: boolean = false;
  isProfileOpen: boolean = false;

  get isLoggedIn() { return this.authService.isLoggedIn(); }
  get userRole() { return this.authService.userRole(); }

  toggleProfile(): void { this.isProfileOpen = !this.isProfileOpen; }
  toggleMobileMenu(): void { this.isMenuOpen = !this.isMenuOpen; }

  logout(): void {
    this.authService.logout();
    this.isProfileOpen = false;
  }
}
