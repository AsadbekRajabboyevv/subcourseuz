import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { PaymentRequestDto, PaymentResponseDto, PaymentFilter } from './payment.model';
import {Base, Page} from "../../common/model/base";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private http = inject(HttpClient);
  private readonly PATH = environment.apiPath + '/v1/api/payments';

  payments = signal<PaymentResponseDto[]>([]);
  isLoading = signal(false);

  purchase(request: PaymentRequestDto): Observable<Base<PaymentResponseDto>> {
    return this.http.post<Base<PaymentResponseDto>>(this.PATH, request);
  }

  getHistory(filter: PaymentFilter, page: number = 0, size: number = 10): Observable<Base<Page<PaymentResponseDto>>> {
    this.isLoading.set(true);

    let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());

    Object.entries(filter).forEach(([key, value]) => {
      if (value) params = params.set(key, value.toString());
    });

    return this.http.get<Base<Page<PaymentResponseDto>>>(this.PATH, { params }).pipe(
      tap(res => {
        this.payments.set(res.data.content);
        this.isLoading.set(false);
      })
    );
  }

  getOne(exId: string): Observable<Base<PaymentResponseDto>> {
    return this.http.get<Base<PaymentResponseDto>>(`${this.PATH}/${exId}`);
  }
}
