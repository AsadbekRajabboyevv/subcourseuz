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
        this.router.navigate(['/auth/login']);
      }
    });
  }
}
