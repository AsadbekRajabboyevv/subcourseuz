import {Component, OnInit, inject} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';
import {PageWrapperComponent} from '../../shared/ui/layout/page-wrapper.component';

@Component({
  selector: 'app-email-confirm',
  standalone: true,
  imports: [CommonModule, RouterLink, PageWrapperComponent],
  template: `
    <app-page-wrapper>
      <div content class="flex min-h-[60vh] items-center justify-center px-4">

        <div
          class="relative w-full max-w-md rounded-[3rem] border border-gray-50 bg-white p-10 shadow-2xl dark:border-gray-800 dark:bg-gray-900 md:p-12">

          <div
            class="absolute -right-16 -top-16 h-48 w-48 rounded-full bg-emerald-500/5 blur-[80px]"></div>
          <div
            class="absolute -left-16 -bottom-16 h-48 w-48 rounded-full bg-blue-500/5 blur-[80px]"></div>

          <div class="relative flex flex-col items-center text-center">

            @if (status === 'success') {
              <div
                class="mb-8 flex h-24 w-24 items-center justify-center rounded-[2.2rem] bg-emerald-50 text-emerald-500 dark:bg-emerald-500/10 shadow-inner">
                <i class="fa-solid fa-envelope-circle-check text-4xl"></i>
              </div>

              <h2
                class="mb-4 text-2xl font-black uppercase tracking-tight text-gray-900 dark:text-white">
                Tasdiqlandi
              </h2>

              <p class="mb-10 text-sm font-medium text-gray-400">
                Elektron pochtangiz muvaffaqiyatli tasdiqlandi. Endi barcha imkoniyatlardan
                foydalanishingiz mumkin.
              </p>

              <button routerLink="/auth/login"
                      class="flex w-full items-center justify-center gap-3 rounded-2xl bg-emerald-600 py-4 text-[11px] font-black uppercase tracking-widest text-white shadow-xl shadow-emerald-500/20 transition-all hover:bg-emerald-700 active:scale-95">
                <span>Login qilish</span>
                <i class="fa-solid fa-unlock text-[10px]"></i>
              </button>
            }

            @if (status === 'error') {
              <div
                class="mb-8 flex h-24 w-24 items-center justify-center rounded-[2.2rem] bg-red-50 text-red-500 dark:bg-red-500/10 shadow-inner">
                <i class="fa-solid fa-triangle-exclamation text-4xl"></i>
              </div>

              <h2
                class="mb-4 text-2xl font-black uppercase tracking-tight text-gray-900 dark:text-white">
                Xatolik
              </h2>

              <p class="mb-10 text-sm font-medium text-gray-400">
                Afsuski, tasdiqlash havolasi yaroqsiz yoki muddati o'tib ketgan. Iltimos, qaytadan
                urinib ko'ring.
              </p>

              <div class="flex w-full flex-col gap-3">
                <button routerLink="/auth/login"
                        class="flex w-full items-center justify-center rounded-2xl bg-gray-900 py-4 text-[11px] font-black uppercase tracking-widest text-white transition-all hover:bg-gray-800 active:scale-95 dark:bg-gray-700">
                  Kirish sahifasiga qaytish
                </button>
              </div>
            }

          </div>
        </div>
      </div>
    </app-page-wrapper>
  `
})
export class EmailConfirmComponent implements OnInit {
  private router = inject(Router);

  status: 'success' | 'error' = 'success';

  ngOnInit() {
    const currentUrl = this.router.url;

    if (currentUrl.includes('confirm-success')) {
      this.status = 'success';
    } else {
      this.status = 'error';
    }
  }
}
