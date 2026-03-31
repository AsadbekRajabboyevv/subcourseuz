import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NgOptimizedImage } from "@angular/common";
import { AuthService } from "../../shared/ui/services/auth.service";
import { ModalService } from "../../shared/ui/services/modal.service";
import { LoginModalComponent } from "../auth/login/login.component";

@Component({
  selector: 'app-header',
  imports: [CommonModule, NgOptimizedImage],
  templateUrl: './app.header.component.html'
})
export class HeaderComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly modalService = inject(ModalService);

  @Input() appTitle: string = 'Subcourse';

  isOpen: boolean = false;

  get currentUser() {
    return this.authService.currentUser();
  }

  get isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  get userRole() {
    return this.authService.currentRole();
  }

  toggleDropdown(): void {
    this.isOpen = !this.isOpen;
  }

  logout(): void {
    this.authService.logout();
  }

  openLoginModal(): void {
    this.modalService.open(LoginModalComponent);
  }

  toggleMobileMenu(): void {
    this.isOpen = !this.isOpen;
  }
}
