import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-oauth2-success',
  template: `
    <div class="flex h-screen items-center justify-center bg-white dark:bg-gray-900">
      <div class="text-center">
        <div class="h-12 w-12 animate-spin rounded-full border-4 border-green-500 border-t-transparent mx-auto"></div>

        <h2 class="mt-6 text-xl font-black uppercase tracking-tighter text-gray-900 dark:text-white">
          Muvaffaqiyatli kirildi
        </h2>
        <p class="mt-2 font-black uppercase tracking-widest text-gray-400 text-[10px]">
          Ma'lumotlar yuklanmoqda, iltimos kuting...
        </p>
      </div>
    </div>
  `
})
export class OAuth2SuccessComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);

  ngOnInit() {
    const token = this.route.snapshot.queryParams['token'];
    let returnUrl = this.route.snapshot.queryParams['returnUrl'];

    if (token) {
      this.authService.me(token).subscribe({
        next: (response) => {
          response.data.bearerToken = token;
          this.authService.setSession(response.data);
          try {
            if (returnUrl) {
              returnUrl = window.atob(returnUrl);
            } else {
              returnUrl = '/';
            }
          } catch (e) {
            returnUrl = '/';
          }

          this.router.navigateByUrl(returnUrl);
        },
        error: (err) => {
          localStorage.removeItem('access_token');
          this.router.navigate(['/auth/login']);
        }
      });
    } else {
      this.router.navigate(['/auth/login']);
    }
  }
}
