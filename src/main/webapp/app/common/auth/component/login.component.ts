import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from "../auth.service";
import {InputComponent} from "../../../shared/ui/forms/input.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, InputComponent],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  loginData = {email: '', password: ''};

  onLogin() {
    this.authService.login(this.loginData).subscribe({
      next: () => this.router.navigate(['/']),
      error: (err) => alert('Login yoki parol xato!')
    });
  }
}
