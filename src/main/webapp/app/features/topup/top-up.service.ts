import {inject, Injectable, signal} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {environment} from '../../../environments/environment';
import {Base, Page} from "app/common/model/base";
import {
  TopUpBalance,
  TopUpRequest,
  TopUpRequestAction,
  TopUpRequestFilter,
  TopUpStatus
} from "./top-up.model";
import {Payment} from "../payment/payment.model";

@Injectable({providedIn: 'root'})
export class TopUpRequestService {
  private readonly http = inject(HttpClient);
  private readonly PATH = `${environment.apiPath}/v1/api/top-up-request`;

  requests = signal<TopUpRequest[]>([]);
  totalLength = signal(0);
  isLoading = signal<boolean>(false);

  getMy(filter: TopUpRequestFilter, page: number, size: number): Observable<Base<Page<TopUpRequest>>> {
    return this.fetchRequests(`${this.PATH}/my`, filter, page, size);
  }

  getAll(filter: TopUpRequestFilter, page: number, size: number): Observable<Base<Page<TopUpRequest>>> {
    return this.fetchRequests(this.PATH, filter, page, size);
  }

  private fetchRequests(url: string, filter: TopUpRequestFilter, page: number, size: number): Observable<Base<Page<TopUpRequest>>> {
    this.isLoading.set(true);

    let params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());

    Object.entries(filter).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, value.toString());
      }
    });

    return this.http.get<Base<Page<TopUpRequest>>>(url, {params}).pipe(
      tap((res) => {
        this.isLoading.set(false);
        if (res.data) {
          this.requests.set(res.data.content);
          this.totalLength.set(res.data.totalElements);
        }
      }),
      catchError((err) => {
        this.isLoading.set(false);
        throw err;
      })
    );
  }

  getMyById(id: number): Observable<Base<TopUpRequest>> {
    return this.http.get<Base<TopUpRequest>>(`${this.PATH}/my/${id}`);
  }

  getById(id: number): Observable<Base<TopUpRequest>> {
    return this.http.get<Base<TopUpRequest>>(`${this.PATH}/${id}`);
  }

  // 4. Yangi so'rov yaratish (MultipartFile bilan)
  create(request: TopUpBalance, screenshot: File): Observable<Base<number>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(request)], {type: 'application/json'}));
    formData.append('screenshot', screenshot);

    return this.http.post<Base<number>>(`${this.PATH}/create`, formData);
  }

  cancel(id: number): Observable<Base<number>> {
    return this.http.put<Base<number>>(`${this.PATH}/cancel/${id}`, {}).pipe(
      tap(() => {
        this.requests.update(list => list.map(req =>
          req.id === id ? {...req, status: 'CANCELLED' as any} : req
        ));
      })
    );
  }

  accept(action: TopUpRequestAction): Observable<Base<Payment>> {
    return this.http.put<Base<Payment>>(`${this.PATH}/admin/accept`, action).pipe(
      tap(() => this.updateStatusInList(action.id, 'APPROVED'))
    );
  }

  reject(action: TopUpRequestAction): Observable<Base<Payment>> {
    return this.http.put<Base<Payment>>(`${this.PATH}/admin/reject`, action).pipe(
      tap(() => this.updateStatusInList(action.id, 'REJECTED'))
    );
  }

  private updateStatusInList(id: number, status: string) {
    this.requests.update(list => list.map(req =>
      req.id === id ? {...req, status: status as any} : req
    ));
  }

  resetRequests() {
    this.requests.set([]);
    this.totalLength.set(0);
  }
}
