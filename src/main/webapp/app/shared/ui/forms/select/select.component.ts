import { Component, forwardRef, Input, signal, computed } from '@angular/core';
import { ControlValueAccessor, FormsModule, NG_VALUE_ACCESSOR } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SelectOption } from '../../interfaces';
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-select',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './select.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true,
    },
  ],
})
export class SelectComponent implements ControlValueAccessor {
  @Input() label = '';
  @Input() options: SelectOption[] = [];
  @Input() multiple = false;
  @Input() searchable = false;
  @Input() placeholder = 'Tanlang...';

  protected value = signal<unknown>(null);
  protected isDisabled = signal(false);
  protected searchQuery = signal('');

  protected filteredOptions = computed(() => {
    const q = this.searchQuery().toLowerCase();
    if (!q) return this.options;
    return this.options.filter(o => o.label.toLowerCase().includes(q));
  });

  private onChange: (v: unknown) => void = () => {};
  private onTouched: () => void = () => {};

  writeValue(value: unknown): void {
    this.value.set(value ?? (this.multiple ? [] : null));
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

  protected onSelectChange(event: Event): void {
    const select = event.target as HTMLSelectElement;
    let val: unknown;
    if (this.multiple) {
      val = Array.from(select.selectedOptions).map(opt => {
        const found = this.options.find(o => String(o.value) === opt.value);
        return found ? found.value : opt.value;
      });
    } else {
      const found = this.options.find(o => String(o.value) === select.value);
      val = found ? found.value : select.value;
    }
    this.value.set(val);
    this.onChange(val);
  }

  protected onBlur(): void {
    this.onTouched();
  }

  protected onSearch(event: Event): void {
    this.searchQuery.set((event.target as HTMLInputElement).value);
  }

  protected isSelected(option: SelectOption): boolean {
    const v = this.value();
    if (this.multiple && Array.isArray(v)) {
      return v.some(item => item === option.value);
    }
    return v === option.value;
  }
}
