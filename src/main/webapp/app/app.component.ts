import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationStart, NavigationEnd, NavigationCancel, NavigationError, Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './common/header/app.header.component';
import { FooterComponent } from './common/footer/app.footer.component';
import { ErrorModalComponent } from './common/error/error-modal.component';
import { PageLoaderComponent } from './common/loading/page-loader.component';
import { PageLoaderService } from './common/loading/page-loader.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    HeaderComponent,
    FooterComponent,
    ErrorModalComponent,
    PageLoaderComponent,
  ],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly pageLoader = inject(PageLoaderService);

  isAuthPage = false;
  msgSuccess: string | null = null;
  msgInfo: string | null = null;
  msgError: string | null = null;

  ngOnInit(): void {
    const savedLang = localStorage.getItem('selectedLanguage') || 'uz';

    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.pageLoader.start();
        const state = this.router.getCurrentNavigation()?.extras.state;
        this.msgSuccess = state?.['msgSuccess'] ?? null;
        this.msgInfo    = state?.['msgInfo']    ?? null;
        this.msgError   = state?.['msgError']   ?? null;
      }

      if (event instanceof NavigationEnd) {
        this.isAuthPage = event.url.startsWith('/auth');
        this.pageLoader.complete();
      }

      if (event instanceof NavigationCancel || event instanceof NavigationError) {
        this.pageLoader.complete();
      }
    });
  }
}
