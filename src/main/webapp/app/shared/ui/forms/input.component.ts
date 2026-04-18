import { Component, Input, forwardRef, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [CommonModule, FormsModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true
    }
  ],
  template: `
    <div class="space-y-2 w-full">
      <label *ngIf="label"
             class="ml-2 text-[10px] font-black uppercase tracking-widest text-gray-400 dark:text-gray-500">
        {{ label }}
      </label>

      <div class="relative group">

        <ng-container *ngIf="['text', 'number', 'date'].includes(type)">
          <input [type]="type"
                 [value]="value"
                 (input)="onValueChange($any($event.target).value)"
                 [placeholder]="placeholder"
                 class="w-full rounded-2xl border-2 border-gray-50 bg-gray-50 px-6 py-4 text-sm font-bold outline-none transition-all
                        hover:border-emerald-500 focus:border-emerald-500 focus:bg-white
                        dark:border-gray-800 dark:bg-gray-800 dark:text-white dark:focus:bg-gray-900 dark:focus:border-emerald-500
                        placeholder:text-gray-400 dark:placeholder:text-gray-600">
        </ng-container>

        <ng-container *ngIf="type === 'trix'">
          <div class="w-full rounded-[2rem] border-2 border-gray-50 bg-gray-50 overflow-hidden transition-all
              hover:border-emerald-500 focus-within:border-emerald-500 focus-within:bg-white
              dark:border-gray-800 dark:bg-gray-800">

            <input [id]="label + '-trix-input'" type="hidden" [value]="value">

            <trix-editor
              [attr.input]="label + '-trix-input'"
              class="trix-content min-h-[400px] p-6 text-sm font-bold dark:text-white outline-none"
              (trix-change)="onTrixChange($event)"
              [attr.placeholder]="placeholder">
            </trix-editor>
          </div>
        </ng-container>

        <ng-container *ngIf="type === 'textarea'">
          <textarea [value]="value"
                    (input)="onValueChange($any($event.target).value)"
                    [placeholder]="placeholder"
                    rows="4"
                    class="w-full rounded-2xl border-2 border-gray-50 bg-gray-50 px-6 py-4 text-sm font-bold outline-none transition-all
                           hover:border-emerald-500 focus:border-emerald-500 focus:bg-white
                           dark:border-gray-800 dark:bg-gray-800 dark:text-white dark:focus:bg-gray-900 resize-none"></textarea>
        </ng-container>

        <ng-container *ngIf="type === 'select'">
          <select [ngModel]="value"
                  (ngModelChange)="onValueChange($event)"
                  class="w-full appearance-none rounded-2xl border-2 border-gray-50 bg-gray-50 px-6 py-4 text-sm font-bold outline-none transition-all
                         hover:border-emerald-500 focus:border-emerald-500
                         dark:border-gray-800 dark:bg-gray-800 dark:text-white dark:focus:bg-gray-900">
            <option value="" disabled class="dark:bg-gray-900">{{ placeholder }}</option>
            <option *ngFor="let opt of options" [value]="opt.value" class="dark:bg-gray-900">{{ opt.label }}</option>
          </select>
          <i class="fa-solid fa-chevron-down absolute right-5 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none group-hover:text-emerald-500"></i>
        </ng-container>

        <div *ngIf="type === 'multi-select'"
             class="flex flex-wrap gap-2 p-4 rounded-2xl border-2 border-gray-50 bg-gray-50 dark:bg-gray-800 dark:border-gray-800 transition-all hover:border-emerald-500">
          <button *ngFor="let opt of options"
                  type="button"
                  (click)="toggleMultiSelect(opt.value)"
                  [ngClass]="isSelected(opt.value)
                    ? 'bg-emerald-500 text-white shadow-lg shadow-emerald-500/20'
                    : 'bg-gray-200 text-gray-500 dark:bg-gray-700 dark:text-gray-400 hover:bg-gray-300 dark:hover:bg-gray-600'"
                  class="px-4 py-2 rounded-xl text-[10px] font-black uppercase transition-all active:scale-95">
            {{ opt.label }}
          </button>
        </div>

        <div *ngIf="type === 'checkbox'"
             (click)="onValueChange(!value)"
             class="flex items-center gap-4 cursor-pointer p-4 rounded-2xl border-2 border-gray-50 bg-gray-50 dark:bg-gray-800 dark:border-gray-800 hover:border-emerald-500 transition-all">
          <div class="w-6 h-6 rounded-lg border-2 flex items-center justify-center transition-all"
               [ngClass]="value ? 'bg-emerald-500 border-emerald-500' : 'border-gray-300 bg-white dark:border-gray-600 dark:bg-gray-900'">
            <i *ngIf="value" class="fa-solid fa-check text-white text-xs"></i>
          </div>
          <span class="text-sm font-bold text-gray-600 dark:text-gray-300">{{ placeholder }}</span>
        </div>

        <div *ngIf="type === 'file'" class="space-y-3">
          <div class="relative w-full rounded-2xl border-2 border-dashed border-gray-100 bg-gray-50 p-6 transition-all hover:border-emerald-500 dark:border-gray-800 dark:bg-gray-800 text-center">
            <input type="file" (change)="handleFileUpload($event)" class="absolute inset-0 z-10 cursor-pointer opacity-0">
            <div class="space-y-2">
              <i class="fa-solid fa-cloud-arrow-up text-2xl text-emerald-500"></i>
              <p class="text-xs font-black uppercase dark:text-white">{{ fileName || placeholder || 'Faylni yuklang' }}</p>
            </div>
          </div>
          <div *ngIf="uploadProgress > 0" class="px-2">
            <div class="h-1.5 w-full rounded-full bg-gray-100 dark:bg-gray-700 overflow-hidden">
              <div class="h-full bg-emerald-500 transition-all duration-300" [style.width.%]="uploadProgress"></div>
            </div>
          </div>
        </div>

      </div>
    </div>
  `,
  styles: [`
    /* Trix Toolbar */
    trix-toolbar {
      border-bottom: 2px solid #f3f4f6 !important; /* gray-100 */
      background-color: rgba(255, 255, 255, 0.5) !important;
      padding: 0.5rem !important;
    }

    .dark trix-toolbar {
      border-bottom-color: #1f2937 !important; /* gray-800 */
      background-color: rgba(31, 41, 55, 0.5) !important;
    }

    trix-toolbar .trix-button-row {
      display: flex !important;
      flex-wrap: wrap !important;
      gap: 0.25rem !important;
    }

    trix-toolbar .trix-button {
      border: none !important;
      background-color: transparent !important;
      border-radius: 0.5rem !important;
      transition: all 0.2s !important;
      color: #4b5563 !important; /* gray-600 */
    }

    .dark trix-toolbar .trix-button {
      color: #9ca3af !important; /* gray-400 */
    }

    trix-toolbar .trix-button:hover {
      background-color: #ecfdf5 !important; /* emerald-50 */
      color: #10b981 !important; /* emerald-500 */
    }

    trix-toolbar .trix-button--active {
      background-color: #10b981 !important;
      color: white !important;
    }

    /* Trix Editor Body */
    trix-editor {
      min-height: 200px !important;
      border: none !important;
      outline: none !important;
      font-weight: 400 !important;
    }

    /* Content Styling */
    .trix-content h1 {
      font-size: 1.25rem !important;
      font-weight: 900 !important;
      margin-bottom: 0.5rem !important;
    }

    .trix-content blockquote {
      border-left: 4px solid #10b981 !important;
      padding-left: 1rem !important;
      font-style: italic !important;
      color: #6b7280 !important;
    }
  `]
})
export class InputComponent implements ControlValueAccessor {
  @Input() type: 'text' | 'number' | 'select' | 'multi-select' | 'checkbox' | 'date' | 'textarea' | 'file' | 'trix' = 'text';
  @Input() label: string = '';
  @Input() placeholder: string = 'Tanlang';
  @Input() options: { label: string, value: any }[] = [];

  value: any = '';
  uploadProgress: number = 0;
  fileName: string = '';

  onChange: any = () => {};
  onTouch: any = () => {};

  onTrixChange(event: any) {
    const newValue = event.target.value;

    if (this.value !== newValue) {
      this.value = newValue;
      this.onChange(newValue);
      this.onTouch();
    }
  }
  onValueChange(newValue: any) {
    this.value = newValue;
    this.onChange(newValue);
  }

  // --- MAVJUD METODLAR ---
  handleFileUpload(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.fileName = file.name;
      this.uploadProgress = 0;
      const interval = setInterval(() => {
        this.uploadProgress += 10;
        if (this.uploadProgress >= 100) {
          clearInterval(interval);
          this.onValueChange(file);
        }
      }, 100);
    }
  }

  toggleMultiSelect(optValue: any) {
    if (!Array.isArray(this.value)) this.value = [];
    const index = this.value.indexOf(optValue);
    index > -1 ? this.value.splice(index, 1) : this.value.push(optValue);
    this.onChange(this.value);
  }

  isSelected(optValue: any): boolean {
    return Array.isArray(this.value) && this.value.includes(optValue);
  }

  writeValue(value: any): void {
    this.value = value;
    const trixElement = document.querySelector('trix-editor');
    if (trixElement && value) {
      (trixElement as any).editor.loadHTML(value);
    }
  }
  registerOnChange(fn: any): void { this.onChange = fn; }
  registerOnTouched(fn: any): void { this.onTouch = fn; }
}
