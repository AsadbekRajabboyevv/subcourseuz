import { Injectable } from '@angular/core';
import { BaseApiService } from "../../../common/services/base.service";
import { Observable } from "rxjs";
import { Base } from "../../../common/model/base";
import {
  ScienceRequest,
  ScienceResponse,
  ScienceUpdateRequest,
  OneScienceResponse
} from "../model/science.model";

@Injectable({
  providedIn: 'root'
})
export class ScienceService extends BaseApiService {
  protected override readonly path = `/v1/api/sciences`;

  getSciences(): Observable<Base<ScienceResponse[]>> {
    return this.get<Base<ScienceResponse[]>>();
  }

  getScienceById(id: number): Observable<Base<OneScienceResponse>> {
    return this.getById<Base<OneScienceResponse>>(id);
  }

  createScience(request: ScienceRequest, image: File): Observable<Base<ScienceResponse>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(request)], {
      type: 'application/json'
    }));
    formData.append('image', image);
    return this.http.post<Base<ScienceResponse>>(`${this.apiUrl}${this.path}`, formData);
  }

  updateScience(id: number, request: ScienceUpdateRequest, image: File | null): Observable<Base<ScienceResponse>> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(request)], {
      type: 'application/json'
    }));
    if (image) {
      formData.append('image', image);
    }

    return this.http.put<Base<ScienceResponse>>(`${this.apiUrl}${this.path}/${id}`, formData);
  }

  deleteScience(id: number): Observable<Base<number>> {
    return this.delete<Base<number>>(id);
  }
}
