import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from "../auth.service";
import {InputComponent} from "../../../shared/ui/forms/input.component";
import {RegisterRequest} from "../auth.model";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, InputComponent],
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  showSuccessModal = false;
  regData: RegisterRequest = {
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    bio: '',
    birthDate: '',
    phone: '',
    position: ''
  };
  onRegister() {
    this.authService.register(this.regData).subscribe({
      next: () => {
        this.showSuccessModal = true;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  closeModalAndLogin() {
    this.showSuccessModal = false;
    this.router.navigate(['/login']);
  }
}
