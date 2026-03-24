import { Component, Input, Output, EventEmitter } from '@angular/core';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-login-modal',
  imports: [
    FormsModule
  ],
  templateUrl: './login-modal.component.html'
})
export class LoginModalComponent {

  @Input() isOpen = false;
  @Output() closeModal = new EventEmitter<void>();
  @Output() openRegister = new EventEmitter<void>();

  email = '';
  password = '';
  error = false;

  close() {
    this.closeModal.emit();
  }

  login() {
    console.log('LOGIN', this.email, this.password);
  }

  switchToRegister() {
    this.close();
    this.openRegister.emit();
  }
}
