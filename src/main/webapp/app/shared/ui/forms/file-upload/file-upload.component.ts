import { Component, EventEmitter, forwardRef, Input, Output, signal } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FileRejectionReason } from '../../interfaces';
import {LucideAngularModule} from "lucide-angular";

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './file-upload.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FileUploadComponent),
      multi: true,
    },
  ],
})
export class FileUploadComponent implements ControlValueAccessor {
  @Input() accept = '*';
  @Input() label = 'Fayl yuklash';
  @Input() maxSizeMb = 10;
  @Input() multiple = false;

  @Output() fileRejected = new EventEmitter<FileRejectionReason>();

  protected selectedFiles = signal<File[]>([]);
  protected isDisabled = signal(false);
  protected isDragOver = signal(false);
  protected rejectionMessage = signal('');

  private onChange: (v: unknown) => void = () => {};
  private onTouched: () => void = () => {};

  writeValue(value: File | File[] | null): void {
    if (!value) {
      this.selectedFiles.set([]);
    } else if (Array.isArray(value)) {
      this.selectedFiles.set(value);
    } else {
      this.selectedFiles.set([value]);
    }
  }

  registerOnChange(fn: (v: unknown) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.isDisabled.set(isDisabled);
  }

  protected onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.processFiles(Array.from(input.files));
    }
    this.onTouched();
  }

  protected onDragOver(event: DragEvent): void {
    event.preventDefault();
    if (!this.isDisabled()) {
      this.isDragOver.set(true);
    }
  }

  protected onDragLeave(): void {
    this.isDragOver.set(false);
  }

  protected onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragOver.set(false);
    if (this.isDisabled()) return;
    const files = event.dataTransfer?.files;
    if (files) {
      this.processFiles(Array.from(files));
    }
    this.onTouched();
  }

  protected removeFile(index: number): void {
    const updated = this.selectedFiles().filter((_, i) => i !== index);
    this.selectedFiles.set(updated);
    this.emitValue(updated);
  }

  private processFiles(files: File[]): void {
    this.rejectionMessage.set('');
    const valid: File[] = [];

    for (const file of files) {
      const reason = this.validateFile(file);
      if (reason) {
        this.fileRejected.emit(reason);
        this.rejectionMessage.set(
          reason === 'size'
            ? `Fayl hajmi ${this.maxSizeMb}MB dan oshmasligi kerak`
            : `Fayl turi qabul qilinmaydi`
        );
        continue;
      }
      valid.push(file);
      if (!this.multiple) break;
    }

    if (valid.length === 0) return;

    const updated = this.multiple ? [...this.selectedFiles(), ...valid] : valid;
    this.selectedFiles.set(updated);
    this.emitValue(updated);
  }

  private validateFile(file: File): FileRejectionReason | null {
    const maxBytes = this.maxSizeMb * 1024 * 1024;
    if (file.size > maxBytes) return 'size';

    if (this.accept && this.accept !== '*') {
      const accepted = this.accept.split(',').map(a => a.trim());
      const matched = accepted.some(pattern => {
        if (pattern.startsWith('.')) {
          return file.name.toLowerCase().endsWith(pattern.toLowerCase());
        }
        if (pattern.endsWith('/*')) {
          return file.type.startsWith(pattern.slice(0, -1));
        }
        return file.type === pattern;
      });
      if (!matched) return 'type';
    }

    return null;
  }

  private emitValue(files: File[]): void {
    if (this.multiple) {
      this.onChange(files);
    } else {
      this.onChange(files[0] ?? null);
    }
  }
}
