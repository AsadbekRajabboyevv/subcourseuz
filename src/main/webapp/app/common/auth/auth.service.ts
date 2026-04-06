import {Injectable, computed, inject, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable, map, tap} from 'rxjs';
import {AuthCredentials, UserInfo} from '../../shared/ui/interfaces';
import {AuthResponse, LoginRequest, RegisterRequest, UserResponse} from "./auth.model";
import {environment} from "../../../environments/environment";
import {Base} from "../model/base";

@Injectable({providedIn: 'root'})
export class AuthService {
  private readonly TOKEN_KEY = 'jwt_token';
  private readonly BASE_URL = environment.apiPath;

  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly USER_KEY = 'auth_user';
  currentUser = signal<UserResponse | null>(null);
  currentRole = computed(() => this.getUser()?.role ?? null);
  currentPermissions = computed(() => this.currentUser()?.permissions ?? []);
  isLoggedIn = computed(() => this.getUser() !== null);

  constructor() {
    this.restoreSession();
  }

  login(credentials: LoginRequest): Observable<void> {
    return this.http
    .post<Base<AuthResponse>>(
      `${this.BASE_URL}/v1/api/auth/login`,
      credentials
    )
    .pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.data.bearerToken);
        localStorage.setItem(this.USER_KEY, JSON.stringify(response.data.user));
        this.currentUser.set(response.data.user);
      }),
      map(() => void 0),
    );
  }

  register(credentials: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.BASE_URL + '/v1/api/auth/register'}`, credentials);
  }

  logout(): void {
    this.clearStorage();
    this.currentUser.set(null);
    this.router.navigate(['/auth/login']);
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

  getUser(): UserResponse | null {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  handleUnauthorized(): void {
    this.clearStorage();
    this.currentUser.set(null);
    this.router.navigate(['/auth/login']);
  }

  private restoreSession(): void {
    const token = this.getToken();
    const user = localStorage.getItem(this.USER_KEY);

    if (!token || !this.isAuthenticated() || !user) return;

    try {
      this.currentUser.set(JSON.parse(user));
    } catch {
      this.clearStorage();
    }
  }

  private clearStorage(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }
}
