import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { HttpClient } from '@angular/common/http';
import {
  BaseResponseDto,
  AuthResponseDto,
  LoginRequestDto,
  RegisterRequestDto
} from './auth.model';
import {
  DatepickerComponent,
  InputComponent,
  SelectComponent
} from "../../shared/ui";

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, LucideAngularModule, DatepickerComponent, SelectComponent, InputComponent],
  templateUrl: './auth.component.html'
})
export class AuthComponent {
  private http = inject(HttpClient);
  private router = inject(Router);

  isLoginView = true;
  loading = false;
  errorMessage = '';
  showPassword = false;

  // Form ma'lumotlari
  loginRequest: LoginRequestDto = { email: '', password: '' };
  registerRequest: RegisterRequestDto = {
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
    this.http.post<BaseResponseDto<AuthResponseDto>>('/v1/api/auth/login', this.loginRequest)
    .subscribe({
      next: (res) => {
        if (res.success) {
          localStorage.setItem('token', res.data.bearerToken);
          this.router.navigate(['/']);
        }
      },
      error: (err) => {
        this.errorMessage = err.error?.message || "Email yoki parol xato!";
        this.loading = false;
      }
    });
  }

  onRegister() {
    this.loading = true;
    this.http.post<BaseResponseDto<AuthResponseDto>>('/v1/api/auth/register', this.registerRequest)
    .subscribe({
      next: (res) => {
        if (res.success) {
          alert("Tasdiqlash xati yuborildi!");
          this.isLoginView = true;
        }
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || "Xatolik yuz berdi!";
        this.loading = false;
      }
    });
  }
}
