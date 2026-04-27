import {Component, Input, inject, HostListener, ElementRef} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from "../auth/auth.service";
import {ThemeSwitchComponent} from "../../shared/ui/menu/theme-switch.component";
import {BalanceWidgetComponent} from "../balance/balance-widget.component";
import {LangSwitcherComponent} from "../../shared/ui/menu/lang-switcher.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, ThemeSwitchComponent, BalanceWidgetComponent, LangSwitcherComponent, TranslatePipe],

  templateUrl: './app.header.component.html'
})
export class HeaderComponent {
  protected readonly authService = inject(AuthService);
  private eRef = inject(ElementRef);
  @Input() appTitle: string = 'Subcourse';
  isMenuOpen: boolean = false;
  isProfileOpen: boolean = false;

  get isLoggedIn() { return this.authService.isLoggedIn(); }
  get userRole() { return this.authService.userRole(); }

  toggleProfile(event: Event) {
    event.stopPropagation();
    this.isProfileOpen = !this.isProfileOpen;
  }
  toggleMobileMenu(): void { this.isMenuOpen = !this.isMenuOpen; }

  @HostListener('document:click', ['$event'])
  clickout(event: MouseEvent) {
    const target = event.target as Node;

    if (!this.eRef.nativeElement.contains(target)) {
      this.isProfileOpen = false;
    }
  }

  @HostListener('document:mousemove', ['$event'])
  onMouseMove(event: MouseEvent) {
    if (this.isProfileOpen) {
      const target = event.target as Node;
      if (!this.eRef.nativeElement.contains(target)) {
        this.isProfileOpen = false;
      }
    }
  }

  logout(): void {
    this.authService.logout();
    this.isProfileOpen = false;
  }
}
