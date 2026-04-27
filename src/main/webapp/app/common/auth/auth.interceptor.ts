import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest
} from '@angular/common/http';
import { inject } from '@angular/core';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { AuthService } from './auth.service';
import {LangService} from "../../shared/ui/services/language.service";

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null | undefined>(undefined);

export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> => {

  const authService = inject(AuthService);
  const langService = inject(LangService);

  const token = authService.getToken();
  const lang = langService.getLang();

  let headers = req.headers.set('Accept-Language', lang);

  if (token) {
    headers = headers.set('Authorization', `Bearer ${token}`);
  }

  const authReq = req.clone({ headers });

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse): Observable<HttpEvent<unknown>> => {
      if (
        error.status === 401 &&
        !req.url.includes('/auth/login') &&
        !req.url.includes('/auth/refresh')
      ) {
        return handle401Error(authService, langService, authReq, next);
      }
      return throwError(() => error);
    })
  );
};

function handle401Error(
  authService: AuthService,
  langService: LangService,
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {

  if (!isRefreshing) {
    isRefreshing = true;
    refreshTokenSubject.next(undefined);

    return authService.refreshToken().pipe(
      switchMap(() => {
        isRefreshing = false;
        const newToken = authService.getToken();
        const currentLang = langService.getLang();

        refreshTokenSubject.next(newToken);

        if (!newToken) {
          return throwError(() => new Error('No token after refresh'));
        }

        return next(req.clone({
          setHeaders: {
            Authorization: `Bearer ${newToken}`,
            'Accept-Language': currentLang
          }
        }));
      }),
      catchError((err) => {
        isRefreshing = false;
        refreshTokenSubject.next(null);
        authService.logout();
        return throwError(() => err);
      })
    );
  }

  return refreshTokenSubject.pipe(
    filter((token): token is string | null => token !== undefined),
    take(1),
    switchMap((token: string | null) => {
      if (!token) {
        return throwError(() => new Error('Refresh token failed'));
      }

      return next(req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
          'Accept-Language': langService.getLang()
        }
      }));
    })
  );
}
