import { Component, inject, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationStart, Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./common/header/app.header.component";
import { FooterComponent } from "./common/footer/app.footer.component";
import { AuthService } from "./shared/ui/services/auth.service";
import { ThemeService } from "./shared/ui/services/theme.service";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";


@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit, OnDestroy {
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);
  private readonly themeService = inject(ThemeService);
  private readonly destroy$ = new Subject<void>();

  msgSuccess: string | null = null;
  msgInfo: string | null = null;
  msgError: string | null = null;

  ngOnInit(): void {
    // Initialize theme on app start
    this.themeService.initializeTheme();
    
    this.router.events
      .pipe(takeUntil(this.destroy$))
      .subscribe((event) => {
        if (event instanceof NavigationStart) {
          const navigationState = this.router.getCurrentNavigation()?.extras.state;
          this.msgSuccess = navigationState?.['msgSuccess'] || null;
          this.msgInfo = navigationState?.['msgInfo'] || null;
          this.msgError = navigationState?.['msgError'] || null;
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
