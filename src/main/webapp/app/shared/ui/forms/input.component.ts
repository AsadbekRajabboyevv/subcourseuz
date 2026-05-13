import {
  Component, Input, forwardRef, CUSTOM_ELEMENTS_SCHEMA, ChangeDetectorRef
} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor} from '@angular/forms';
import {MarkdownEditorComponent} from "./markdown-editor/markdown-editor.component";

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [CommonModule, FormsModule, MarkdownEditorComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputComponent),
    multi: true
  }],
  templateUrl: './input.component.html',
  styleUrl: './input.component.scss'
})
export class InputComponent implements ControlValueAccessor {
  @Input() type: 'text' | 'number' | 'select' | 'multi-select' | 'checkbox' | 'date' | 'textarea' | 'file' | 'markdown' | 'amount' = 'text';
  @Input() label: string = '';
  @Input() placeholder: string = 'Tanlang';
  @Input() options: { label: string, value: any }[] = [];
  @Input() disabled: boolean = false;

  value: any = '';
  uploadProgress = 0;
  fileName = '';
  formattedAmount: string = '';

  onChange: any = () => {
  };
  onTouch: any = () => {
  };

  constructor(private cdr: ChangeDetectorRef) {
  }

  writeValue(value: any): void {
    this.value = value ?? '';
    if (this.type === 'amount') {
      this.formattedAmount = this.formatAmount(this.value);
    }
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onValueChange(newValue: any): void {
    if (this.disabled) return;
    this.value = newValue;
    this.onChange(newValue);
    this.onTouch();
  }

  onAmountInput(raw: string): void {
    const digits = raw.replace(/[^0-9]/g, '');
    const numeric = digits ? Number(digits) : null;
    this.formattedAmount = digits ? this.formatAmount(Number(digits)) : '';
    this.onValueChange(numeric);
  }

  private formatAmount(value: number | null): string {
    if (value === null || value === undefined) return '';
    return new Intl.NumberFormat('en-US').format(value);
  }

  handleFileUpload(event: any): void {
    const file = event.target.files[0];
    if (!file) return;
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

  toggleMultiSelect(optValue: any): void {
    if (!Array.isArray(this.value)) this.value = [];
    const i = this.value.indexOf(optValue);
    i > -1 ? this.value.splice(i, 1) : this.value.push(optValue);
    this.onChange([...this.value]);
    this.onTouch();
  }

  isSelected(optValue: any): boolean {
    return Array.isArray(this.value) && this.value.includes(optValue);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }
}
