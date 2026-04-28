import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {AuthService} from "../auth.service";
import {InputComponent} from "../../../shared/ui/forms/input.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    InputComponent
  ],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  protected route = inject(ActivatedRoute);

  loginData = {email: '', password: ''};

  onLogin() {
    this.authService.login(this.loginData).subscribe({
      next: () => {
        let returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

        if (returnUrl.includes('/courses-view')) {
          const separator = returnUrl.includes('?') ? '&' : '?';
          returnUrl += `${separator}buyNow=true`;
        }

        this.router.navigateByUrl(returnUrl);
      }
    });
  }
}
