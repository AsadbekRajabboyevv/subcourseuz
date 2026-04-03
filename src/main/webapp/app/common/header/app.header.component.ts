import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgOptimizedImage } from "@angular/common";
import { AuthService } from "../../shared/ui/services/auth.service";
import { ThemeSwitchComponent } from "../../shared/ui";
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage, ThemeSwitchComponent, RouterModule, LucideAngularModule],
  templateUrl: './app.header.component.html'
})
export class HeaderComponent {
  private readonly authService = inject(AuthService);

  @Input() appTitle: string = 'Subcourse';

  isMenuOpen: boolean = false;
  isProfileOpen: boolean = false;

  get isLoggedIn() { return this.authService.isLoggedIn(); }
  get userRole() { return this.authService.currentRole(); }

  toggleProfile(): void { this.isProfileOpen = !this.isProfileOpen; }
  toggleMobileMenu(): void { this.isMenuOpen = !this.isMenuOpen; }

  logout(): void {
    this.authService.logout();
    this.isProfileOpen = false;
  }
}
