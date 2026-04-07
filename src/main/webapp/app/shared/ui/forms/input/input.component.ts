import { Component, forwardRef, Input, signal } from '@angular/core';
import { ControlValueAccessor, FormsModule, NG_VALUE_ACCESSOR } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgIconsModule, provideIcons } from '@ng-icons/core';
import { heroEnvelope, heroExclamationCircle, heroEye, heroEyeSlash, heroLockClosed, heroPhone, heroUser } from '@ng-icons/heroicons/outline';

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [CommonModule, FormsModule, NgIconsModule],
  providers: [
    provideIcons({ heroUser, heroEnvelope, heroPhone, heroLockClosed, heroEye, heroEyeSlash, heroExclamationCircle }),
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => InputComponent),
      multi: true,
    },
  ],
  templateUrl: './input.component.html',
})
export class InputComponent implements ControlValueAccessor {
  @Input() label = '';
  @Input() placeholder = '';
  @Input() type: 'text' | 'email' | 'password' | 'number' | 'tel' = 'text';
  @Input() icon: string = '';
  @Input() disabled = false;
  @Input() errorMessage = '';

  protected value = signal<unknown>('');
  protected isDisabled = signal(false);
  protected showPassword = signal(false); // Parol ko'rinishi uchun
  protected iconMap: Record<string, string> = {
    user: 'heroUser',
    mail: 'heroEnvelope',
    phone: 'heroPhone',
    lock: 'heroLockClosed',
  };

  private onChange: (v: unknown) => void = () => {};
  private onTouched: () => void = () => {};

  writeValue(value: unknown): void { this.value.set(value ?? ''); }
  registerOnChange(fn: (v: unknown) => void): void { this.onChange = fn; }
  registerOnTouched(fn: () => void): void { this.onTouched = fn; }
  setDisabledState(isDisabled: boolean): void { this.isDisabled.set(isDisabled); }

  protected onInput(event: Event): void {
    const val = (event.target as HTMLInputElement).value;
    this.value.set(val);
    this.onChange(val);
  }

  protected onBlur(): void { this.onTouched(); }

  protected getInputType(): string {
    if (this.type === 'password') {
      return this.showPassword() ? 'text' : 'password';
    }
    return this.type;
  }
}
