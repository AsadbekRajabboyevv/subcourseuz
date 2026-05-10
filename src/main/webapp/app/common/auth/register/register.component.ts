import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html'
})
export class RegisterModalComponent {

  @Input() isOpen = false;
  @Output() closeModal = new EventEmitter<void>();
  @Output() openLogin = new EventEmitter<void>();

  form: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    birthDate: string;
    password: string;
    confirmPassword: string;
  } = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    birthDate: '',
    password: '',
    confirmPassword: ''
  };

  loading = false;
  error = false;
  errorMessage = '';
  success = false;
  showPassword = false;
  showConfirmPassword = false;

  get passwordMismatch(): boolean {
    return !!this.form.confirmPassword && this.form.password !== this.form.confirmPassword;
  }

  close() {
    this.closeModal.emit();
  }

  onBackdropClick(event: MouseEvent) {
    if ((event.target as HTMLElement).classList.contains('fixed')) {
      this.close();
    }
  }

  register() {
    if (this.passwordMismatch) return;
    this.loading = true;
    this.error = false;
    this.success = false;
    // TODO: inject AuthService and call register API
    console.log('REGISTER', this.form);
    this.loading = false;
  }

  switchToLogin() {
    this.close();
    this.openLogin.emit();
  }
}
