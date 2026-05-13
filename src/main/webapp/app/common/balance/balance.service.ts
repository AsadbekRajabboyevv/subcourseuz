import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {UserBalance} from "./balance.model";
import {environment} from "../../../environments/environment";
import {Base} from "../model/base";

@Injectable({
  providedIn: 'root'
})
export class BalanceService {
  private http = inject(HttpClient);
  private readonly PATH = `${environment.apiPath}/v1/api/balance`;

  getMyBalance(): Observable<Base<UserBalance>> {
    return this.http.get<Base<UserBalance>>(`${this.PATH}/my-balance`);
  }
}
