import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpEventType } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../../../environments/environment';

export interface UploadedFile {
  url: string;
  name: string;
  mimeType: string;
  size: number;
}

export interface UploadProgress {
  progress: number;
  url?: string;
}

@Injectable({ providedIn: 'root' })
export class MarkdownFileUploadService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiPath}/v1/api/files`;

  readonly MAX_SIZE_BYTES = 50 * 1024 * 1024; // 50MB

  readonly ACCEPTED_IMAGES = ['image/jpeg', 'image/png', 'image/gif', 'image/webp', 'image/svg+xml'];
  readonly ACCEPTED_DOCS = [
    'application/pdf',
    'application/msword',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    'application/vnd.ms-powerpoint',
    'application/vnd.openxmlformats-officedocument.presentationml.presentation',
  ];
  readonly ACCEPTED_ARCHIVES = [
    'application/zip',
    'application/x-rar-compressed',
    'application/x-7z-compressed',
    'application/java-archive',
  ];

  get allAccepted(): string[] {
    return [...this.ACCEPTED_IMAGES, ...this.ACCEPTED_DOCS, ...this.ACCEPTED_ARCHIVES];
  }

  isImage(mimeType: string): boolean {
    return this.ACCEPTED_IMAGES.includes(mimeType);
  }

  validate(file: File): string | null {
    if (file.size > this.MAX_SIZE_BYTES) {
      return `Fayl hajmi 50MB dan oshmasligi kerak (${(file.size / 1024 / 1024).toFixed(1)}MB)`;
    }
    if (!this.allAccepted.includes(file.type)) {
      return `Qo'llab-quvvatlanmaydigan fayl turi: ${file.type || file.name.split('.').pop()}`;
    }
    return null;
  }

  upload(file: File): Observable<UploadProgress> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(this.apiUrl, formData, {
      reportProgress: true,
      observe: 'events',
      responseType: 'json',
    }).pipe(
      map((event): UploadProgress => {
        if (event.type === HttpEventType.UploadProgress) {
          const progress = event.total ? Math.round(100 * event.loaded / event.total) : 0;
          return { progress };
        }
        if (event.type === HttpEventType.Response) {
          const body = event.body as { data?: string; url?: string } | null;
          const url = body?.data ?? body?.url ?? '';
          return { progress: 100, url };
        }
        return { progress: 0 };
      })
    );
  }

  getFileIcon(mimeType: string): string {
    if (this.isImage(mimeType)) return '🖼️';
    if (mimeType === 'application/pdf') return '📄';
    if (mimeType.includes('word')) return '📝';
    if (mimeType.includes('excel') || mimeType.includes('spreadsheet')) return '📊';
    if (mimeType.includes('powerpoint') || mimeType.includes('presentation')) return '📑';
    if (mimeType.includes('zip') || mimeType.includes('rar') || mimeType.includes('7z')) return '🗜️';
    return '📎';
  }
}
