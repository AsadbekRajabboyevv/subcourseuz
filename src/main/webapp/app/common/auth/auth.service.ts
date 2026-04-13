import {Injectable, computed, inject, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable, throwError} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';
import {AuthResponse, LoginRequest, RegisterRequest, UserResponse} from "./auth.model";
import {environment} from "../../../environments/environment";
import {Base} from "../model/base";

@Injectable({providedIn: 'root'})
export class AuthService {

  private readonly PATH = `${environment.apiPath}/v1/api/auth`;
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private readonly TOKEN_KEY = 'access_token';
  private readonly USER_KEY = 'auth_user';

  private accessToken = signal<string | null>(this.loadToken());
  currentUser = signal<UserResponse | null>(this.loadUser());

  isLoggedIn = computed(() => !!this.accessToken());
  userRole = computed(() => this.currentUser()?.role ?? null);
  isAdmin = computed(() => this.userRole() === 'ROLE_ADMIN');

  login(request: LoginRequest): Observable<void> {
    return this.http.post<Base<AuthResponse>>(`${this.PATH}/login`, request, {
      withCredentials: true
    }).pipe(
      tap(response => this.setSession(response.data)),
      map(() => void 0)
    );
  }

  register(request: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.PATH}/register`, request);
  }

  refreshToken(): Observable<void> {
    return this.http.post<Base<AuthResponse>>(`${this.PATH}/refresh`, {}, {
      withCredentials: true
    }).pipe(
      tap(response => this.setSession(response.data)),
      map(() => void 0),
      catchError(err => {
        this.clearSession();
        return throwError(() => err);
      })
    );
  }

  logout(): void {
    this.http.post(`${this.PATH}/logout`, {}, {
      withCredentials: true
    }).subscribe();

    this.clearSession();
    this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return this.accessToken();
  }

  private setSession(res: AuthResponse) {
    this.accessToken.set(res.bearerToken);
    this.currentUser.set(res.user);

    localStorage.setItem(this.TOKEN_KEY, res.bearerToken);
    localStorage.setItem(this.USER_KEY, JSON.stringify(res.user));
  }

  private clearSession() {
    this.accessToken.set(null);
    this.currentUser.set(null);

    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }

  private loadToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private loadUser(): UserResponse | null {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }
}
