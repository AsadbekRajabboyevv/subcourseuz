import {
  HttpErrorResponse,
  HttpInterceptorFn,
} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";
import {ErrorData} from "./error.model";
import {ErrorService} from "./error.service";
import {inject} from "@angular/core";

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const errorService = inject(ErrorService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {

      let errorData: ErrorData = {
        title: 'Xatolik',
        message: 'Noma’lum xatolik yuz berdi',
        code: error.status.toString()
      };

      if (error.status === 0) {
        errorData.title = 'Aloqa yo‘q';
        errorData.message = 'Server bilan bog‘lanib bo‘lmadi. Internetni tekshiring.';
      } else {
        const err = error.error;
        const code = err?.errorCode;
        const msg = err?.errorMessage;

        errorData.code = code;

        switch (code) {
          case 'INSUFFICIENT_BALANCE':
            errorData.title = 'Mablag‘ yetarli emas';
            errorData.message = 'Hisobingizni to‘ldiring yoki boshqa kurs tanlang.';
            break;
          case 'UNAUTHORIZED':
            errorData.title = 'Kirish taqiqlangan';
            errorData.message = 'Sessiya muddati tugadi, iltimos qayta tizimga kiring.';
            break;
          case 'VALIDATION_FAILED':
            errorData.title = 'Ma’lumot xatosi';
            errorData.message = 'Formada xatolik bor. Iltimos, barcha maydonlarni tekshiring.';
            break;
          default:
            errorData.message = msg || 'Server so‘rovni bajara olmadi.';
        }
      }

      errorService.show(errorData);
      return throwError(() => error);
    })
  );
}
