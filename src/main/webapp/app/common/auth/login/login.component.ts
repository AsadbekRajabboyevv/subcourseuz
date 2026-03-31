import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../shared/ui/services/auth.service';
import { NotificationService } from '../../../shared/ui/services/notification.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginModalComponent {
  private readonly authService = inject(AuthService);
  private readonly notificationService = inject(NotificationService);
  private readonly router = inject(Router);

  @Input() isOpen = false;
  @Output() closeModal = new EventEmitter<void>();
  @Output() openRegister = new EventEmitter<void>();

  email = '';
  password = '';
  error = false;
  errorMessage = '';
  loading = false;
  showPassword = false;

  close(): void {
    this.closeModal.emit();
  }

  onBackdropClick(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('fixed')) {
      this.close();
    }
  }

  login(): void {
    if (!this.email || !this.password) {
      this.errorMessage = 'Please enter email and password';
      this.error = true;
      return;
    }

    this.loading = true;
    this.error = false;

    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: () => {
        this.notificationService.success('Login successful!');
        this.close();
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.errorMessage = error.message || 'Login failed. Please try again.';
        this.error = true;
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  switchToRegister(): void {
    this.close();
    this.openRegister.emit();
  }
}
