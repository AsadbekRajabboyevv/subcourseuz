import { Injectable, inject } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  private authService = inject(AuthService);

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = this.authService.getToken();

    const isAuthRequest = req.url.includes('/auth');

    let authReq = req;

    if (token && !isAuthRequest) {
      authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {

        if (error.status === 401) {
          this.authService.handleUnauthorized();
        }

        return throwError(() => error);
      })
    );
  }
}
