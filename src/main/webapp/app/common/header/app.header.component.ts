import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from "../auth/auth.service";
import {ThemeSwitchComponent} from "../../shared/ui/menu/theme-switch.component";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, ThemeSwitchComponent],

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
