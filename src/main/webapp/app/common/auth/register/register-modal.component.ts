import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-register-modal',
  templateUrl: './register-modal.component.html'
})
export class RegisterModalComponent {

  @Input() isOpen = false;
  @Output() closeModal = new EventEmitter<void>();
  @Output() openLogin = new EventEmitter<void>();

  form: any = {};

  close() {
    this.closeModal.emit();
  }

  register() {
    console.log('REGISTER', this.form);
  }

  switchToLogin() {
    this.close();
    this.openLogin.emit();
  }
}
