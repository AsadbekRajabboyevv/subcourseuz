import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgIconsModule, provideIcons } from '@ng-icons/core';
import {
  heroAcademicCap,
  heroArrowRightOnRectangle,
  heroEnvelope,
  heroExclamationCircle,
  heroEye,
  heroEyeSlash,
  heroLockClosed,
  heroShieldCheck,
  heroUserPlus
} from '@ng-icons/heroicons/outline';
import { HttpClient } from '@angular/common/http';
import {
  LoginRequest,
  RegisterRequest
} from './auth.model';
import {
  AuthService,
  DatepickerComponent,
  InputComponent,
  SelectComponent
} from "../../shared/ui";

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, NgIconsModule, DatepickerComponent, SelectComponent, InputComponent],
  providers: [provideIcons({
    heroAcademicCap,
    heroShieldCheck,
    heroExclamationCircle,
    heroEnvelope,
    heroLockClosed,
    heroEye,
    heroEyeSlash,
    heroArrowRightOnRectangle,
    heroUserPlus
  })],
  templateUrl: './auth.component.html'
})
export class AuthComponent {
  private http = inject(HttpClient);
  private router = inject(Router);
  private authService = inject(AuthService);

  isLoginView = true;
  loading = false;
  errorMessage = '';
  showPassword = false;

  loginRequest: LoginRequest = { email: '', password: '' };
  registerRequest: RegisterRequest = {
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    phone: '',
    birthDate: '',
    position: 'TALABA'
  };

  positions = [
    { value: 'TALABA', label: 'Talaba' },
    { value: 'OQUVCHI', label: "O'quvchi" },
    { value: 'OQITUVCHI', label: "O'qituvchi" },
    { value: 'OTA_ONA', label: 'Ota-ona' },
    { value: 'BOSHQA', label: 'Boshqa' }
  ];

  toggleView() {
    this.isLoginView = !this.isLoginView;
    this.errorMessage = '';
  }

  onLogin() {
    this.loading = true;
    this.errorMessage = '';

    this.authService.login(this.loginRequest)
    .subscribe({
      next: () => {
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.errorMessage =
          err.error?.message || "Email yoki parol xato!";
        this.loading = false;
      },
      complete: () => this.loading = false
    });
  }

  onRegister() {
    this.loading = true;
    this.errorMessage = '';

    this.authService.register(this.registerRequest)
    .subscribe({
      next: () => {
        alert("Tasdiqlash xati yuborildi!");
        this.isLoginView = true;
      },
      error: (err) => {
        this.errorMessage =
          err.error?.message || "Xatolik yuz berdi!";
        this.loading = false;
      },
      complete: () => this.loading = false
    });
  }
}
