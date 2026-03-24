import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationStart, Router, RouterOutlet } from '@angular/router';
import { HeaderComponent } from "./common/header/app.header.component";
import {FooterComponent} from "./common/footer/app.footer.component";


@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, HeaderComponent, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {

  router = inject(Router);

  msgSuccess = null;
  msgInfo = null;
  msgError = null;

  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        const navigationState = this.router.getCurrentNavigation()?.extras.state;
        this.msgSuccess = navigationState?.['msgSuccess'] || null;
        this.msgInfo = navigationState?.['msgInfo'] || null;
        this.msgError = navigationState?.['msgError'] || null;
      }
    });
  }

}
