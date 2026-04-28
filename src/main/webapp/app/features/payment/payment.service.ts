import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { PaymentRequest, Payment, PaymentFilter } from './payment.model';
import {Base, Page} from "../../common/model/base";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private http = inject(HttpClient);
  private readonly PATH = environment.apiPath + '/v1/api/payments';

  payments = signal<Payment[]>([]);
  isLoading = signal(false);

  purchase(request: PaymentRequest): Observable<Base<Payment>> {
    return this.http.post<Base<Payment>>(this.PATH, request);
  }

  getHistory(filter: PaymentFilter, page: number = 0, size: number = 10): Observable<Base<Page<Payment>>> {
    this.isLoading.set(true);

    let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());

    Object.entries(filter).forEach(([key, value]) => {
      if (value) params = params.set(key, value.toString());
    });

    return this.http.get<Base<Page<Payment>>>(this.PATH, { params }).pipe(
      tap(res => {
        this.payments.set(res.data.content);
        this.isLoading.set(false);
      })
    );
  }

  getOne(exId: string): Observable<Base<Payment>> {
    return this.http.get<Base<Payment>>(`${this.PATH}/${exId}`);
  }
}
