import { Injectable, signal } from '@angular/core';
import {ErrorData} from "./error.model";

@Injectable({ providedIn: 'root' })
export class ErrorService {
  errorData = signal<ErrorData | null>(null);

  show(data: ErrorData) {
    this.errorData.set(data);
  }

  close() {
    this.errorData.set(null);
  }
}
