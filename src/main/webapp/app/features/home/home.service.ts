import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { HomePageData } from './home.model';

interface Base<T> { data: T; success: boolean; }

@Injectable({ providedIn: 'root' })
export class HomeService {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiPath}/v1/api/public`;

  getHomePage(): Observable<Base<HomePageData>> {
    return this.http.get<Base<HomePageData>>(`${this.url}/home`);
  }
}
