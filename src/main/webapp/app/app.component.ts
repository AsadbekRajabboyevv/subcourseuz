import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationStart, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./common/header/app.header.component";
import { FooterComponent } from "./common/footer/app.footer.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private router = inject(Router);

  isAuthPage = false;
  msgSuccess: string | null = null;
  msgInfo: string | null = null;
  msgError: string | null = null;

  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.isAuthPage = event.url.startsWith('/auth');
      }

      if (event instanceof NavigationStart) {
        const navigationState = this.router.getCurrentNavigation()?.extras.state;
        this.msgSuccess = navigationState?.['msgSuccess'] || null;
        this.msgInfo = navigationState?.['msgInfo'] || null;
        this.msgError = navigationState?.['msgError'] || null;
      }
    });
  }
}
