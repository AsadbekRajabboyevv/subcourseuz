import {
  HttpErrorResponse,
  HttpInterceptorFn,
} from "@angular/common/http";
import { catchError } from "rxjs/operators";
import { throwError } from "rxjs";
import { ErrorData } from "./error.model";
import { ErrorService } from "./error.service";
import { inject } from "@angular/core";

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const errorService = inject(ErrorService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      const serverError = error.error;

      const errorData: ErrorData = {
        title: serverError?.title || error.statusText || String(error.status),
        message: serverError?.errorMessage || error.message,
        code: serverError?.errorCode || String(error.status)
      };

      if (error.status === 0) {
        errorData.title = "Connection Error";
        errorData.message = "Network unreachable";
      }

      errorService.show(errorData);
      return throwError(() => error);
    })
  );
};
