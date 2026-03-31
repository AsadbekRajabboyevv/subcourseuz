import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, map, tap } from 'rxjs';
import { AuthCredentials, UserInfo } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'jwt_token';

  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  currentUser = signal<UserInfo | null>(null);
  currentRole = computed(() => this.currentUser()?.role ?? null);
  currentPermissions = computed(() => this.currentUser()?.permissions ?? []);
  isLoggedIn = computed(() => this.currentUser() !== null);

  constructor() {
    this.restoreSession();
  }

  login(credentials: AuthCredentials): Observable<void> {
    return this.http
      .post<{ token: string; user: UserInfo }>('/v1/api/auth/login', credentials)
      .pipe(
        tap(response => {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          this.currentUser.set(response.user);
        }),
        map(() => void 0),
      );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  handleUnauthorized(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  private restoreSession(): void {
    const token = this.getToken();
    if (!token || !this.isAuthenticated()) return;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      this.currentUser.set({
        id: payload.id ?? 0,
        email: payload.sub ?? '',
        role: payload.role ?? '',
        permissions: payload.permissions ?? [],
      });
    } catch {
      localStorage.removeItem(this.TOKEN_KEY);
    }
  }
}
