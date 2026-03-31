import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { FieldConfig, ValidatorConfig } from '../../interfaces';
import { AuthService } from '../../services/auth.service';
import { InputComponent } from '../input/input.component';
import { SelectComponent } from '../select/select.component';

@Component({
  selector: 'app-dynamic-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, InputComponent, SelectComponent],
  templateUrl: './dynamic-form.component.html',
})
export class DynamicFormComponent implements OnInit {
  @Input() fields: FieldConfig[] = [];
  @Input() submitLabel = 'Yuborish';
  @Output() formSubmit = new EventEmitter<Record<string, unknown>>();

  form!: FormGroup;

  private authService = inject(AuthService);

  ngOnInit(): void {
    this.buildForm();
  }

  private buildForm(): void {
    const controls: Record<string, FormControl> = {};
    for (const field of this.fields) {
      const validators = this.buildValidators(field);
      controls[field.key] = new FormControl(null, validators);
    }
    this.form = new FormGroup(controls);
  }

  private buildValidators(field: FieldConfig): ValidatorFn[] {
    const validators: ValidatorFn[] = [];

    if (field.required) {
      validators.push(Validators.required);
    }

    for (const v of field.validators ?? []) {
      switch (v.type) {
        case 'minLength':
          validators.push(Validators.minLength(v.value as number));
          break;
        case 'maxLength':
          validators.push(Validators.maxLength(v.value as number));
          break;
        case 'pattern':
          validators.push(Validators.pattern(v.value as string));
          break;
        case 'email':
          validators.push(Validators.email);
          break;
        case 'custom':
          if (v.fn) {
            validators.push(this.buildCustomValidator(v));
          }
          break;
      }
    }

    return validators;
  }

  private buildCustomValidator(v: ValidatorConfig): ValidatorFn {
    return (control: AbstractControl) => {
      if (!v.fn) return null;
      return v.fn(control.value) ? null : { custom: { message: v.message } };
    };
  }

  isFieldVisible(field: FieldConfig): boolean {
    // Role check
    const role = this.authService.currentRole();
    if (field.roles && field.roles.length > 0) {
      if (!role || !field.roles.includes(role)) {
        return false;
      }
    }

    // dependsOn check
    if (field.dependsOn) {
      const depValue = this.form?.get(field.dependsOn.field)?.value;
      return depValue === field.dependsOn.value;
    }

    return true;
  }

  getErrorMessage(field: FieldConfig): string {
    const control = this.form?.get(field.key);
    if (!control || !control.errors || !control.touched) return '';

    const errors = control.errors;

    if (errors['required']) {
      return "Bu maydon to'ldirilishi shart";
    }
    if (errors['minlength']) {
      return `Kamida ${errors['minlength'].requiredLength} ta belgi kiriting`;
    }
    if (errors['maxlength']) {
      return `Ko'pi bilan ${errors['maxlength'].requiredLength} ta belgi kiriting`;
    }
    if (errors['email']) {
      return "To'g'ri email manzil kiriting";
    }
    if (errors['pattern']) {
      return "Noto'g'ri format";
    }
    if (errors['custom']) {
      return errors['custom'].message ?? "Noto'g'ri qiymat";
    }

    // Fallback to validator config message
    for (const v of field.validators ?? []) {
      if (errors[v.type]) return v.message;
    }

    return '';
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.formSubmit.emit(this.form.value as Record<string, unknown>);
    } else {
      this.form.markAllAsTouched();
    }
  }

  reset(): void {
    this.form.reset();
  }
}
