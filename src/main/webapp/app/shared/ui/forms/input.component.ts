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
    <div class="space-y-2 w-full" [class.opacity-50]="disabled" [class.pointer-events-none]="disabled">
      <label *ngIf="label" class="ml-2 text-[10px] font-black uppercase tracking-widest text-gray-400 dark:text-gray-500">
        {{ label }}
      </label>

      <div class="relative group">
        <ng-container *ngIf="['text', 'number', 'date'].includes(type)">
          <input [type]="type" [value]="value" [disabled]="disabled"
                 (input)="onValueChange($any($event.target).value)"
                 [placeholder]="placeholder"
                 class="w-full rounded-2xl border-2 border-gray-50 bg-gray-50 px-6 py-4 text-sm font-bold outline-none transition-all
                    disabled:cursor-not-allowed hover:border-emerald-500 focus:border-emerald-500 ...">
        </ng-container>

        <ng-container *ngIf="type === 'trix'">
          <div class="w-full rounded-[2rem] border-2 border-gray-50 bg-gray-50 overflow-hidden transition-all"
               [class.opacity-60]="disabled">
            <input [id]="label + '-trix-input'" type="hidden" [value]="value">
            <trix-editor [attr.input]="label + '-trix-input'"
                         [attr.contenteditable]="!disabled"
                         class="trix-content min-h-[400px] p-6 text-sm font-bold dark:text-white outline-none ...">
            </trix-editor>
          </div>
        </ng-container>

        <ng-container *ngIf="type === 'textarea'">
      <textarea [value]="value" [disabled]="disabled"
                (input)="onValueChange($any($event.target).value)"
                [placeholder]="placeholder" rows="4"
                class="w-full rounded-2xl border-2 border-gray-50 bg-gray-50 px-6 py-4 text-sm font-bold outline-none ... disabled:cursor-not-allowed"></textarea>
        </ng-container>

        <ng-container *ngIf="type === 'select'">
          <select [ngModel]="value" [disabled]="disabled"
                  (ngModelChange)="onValueChange($event)"
                  class="w-full appearance-none rounded-2xl border-2 border-gray-50 bg-gray-50 px-6 py-4 text-sm font-bold outline-none ... disabled:cursor-not-allowed">
            <option value="" disabled>{{ placeholder }}</option>
            <option *ngFor="let opt of options" [value]="opt.value">{{ opt.label }}</option>
          </select>
          <i class="fa-solid fa-chevron-down absolute right-5 top-1/2 -translate-y-1/2 text-gray-400"></i>
        </ng-container>

        <div *ngIf="type === 'multi-select'" class="flex flex-wrap gap-2 p-4 ...">
          <button *ngFor="let opt of options" type="button"
                  [disabled]="disabled"
                  (click)="toggleMultiSelect(opt.value)"
                  class="px-4 py-2 rounded-xl text-[10px] font-black uppercase transition-all disabled:opacity-50 disabled:cursor-not-allowed ...">
            {{ opt.label }}
          </button>
        </div>

        <div *ngIf="type === 'checkbox'"
             (click)="!disabled && onValueChange(!value)"
             class="flex items-center gap-4 p-4 rounded-2xl border-2 border-gray-50 bg-gray-50 ... "
             [class.cursor-not-allowed]="disabled">
          <div class="w-6 h-6 rounded-lg border-2 flex items-center justify-center transition-all"
               [ngClass]="value ? 'bg-emerald-500 border-emerald-500' : 'border-gray-300 bg-white ...'">
            <i *ngIf="value" class="fa-solid fa-check text-white text-xs"></i>
          </div>
          <span class="text-sm font-bold text-gray-600 dark:text-gray-300">{{ placeholder }}</span>
        </div>

        <div *ngIf="type === 'file'" class="space-y-3">
          <div class="relative w-full rounded-2xl border-2 border-dashed p-6 transition-all"
               [class.opacity-50]="disabled" [class.cursor-not-allowed]="disabled">
            <input type="file" [disabled]="disabled" (change)="handleFileUpload($event)"
                   class="absolute inset-0 z-10 opacity-0" [class.cursor-pointer]="!disabled">
            <div class="space-y-2">
              <i class="fa-solid fa-cloud-arrow-up text-2xl text-emerald-500"></i>
              <p class="text-xs font-black uppercase dark:text-white">{{ fileName || placeholder }}</p>
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
  @Input() disabled: boolean = false;

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
  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
  onValueChange(newValue: any) {
    if (this.disabled) return;
    this.value = newValue;
    this.onChange(newValue);
  }
  registerOnChange(fn: any): void { this.onChange = fn; }
  registerOnTouched(fn: any): void { this.onTouch = fn; }
}
