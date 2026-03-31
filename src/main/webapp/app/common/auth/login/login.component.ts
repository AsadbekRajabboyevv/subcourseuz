import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class LoginModalComponent {

  @Input() isOpen = false;
  @Output() closeModal = new EventEmitter<void>();
  @Output() openRegister = new EventEmitter<void>();

  email = '';
  password = '';
  error = false;
  errorMessage = '';
  loading = false;
  showPassword = false;

  close() {
    this.closeModal.emit();
  }

  onBackdropClick(event: MouseEvent) {
    if ((event.target as HTMLElement).classList.contains('fixed')) {
      this.close();
    }
  }

  login() {
    if (!this.email || !this.password) return;
    this.loading = true;
    this.error = false;
    // TODO: inject AuthService and call login API
    console.log('LOGIN', this.email, this.password);
    this.loading = false;
  }

  switchToRegister() {
    this.close();
    this.openRegister.emit();
  }
}
