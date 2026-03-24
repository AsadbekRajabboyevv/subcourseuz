import { Component, Input } from '@angular/core';
import {NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-header',
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './app.header.component.html'
})
export class HeaderComponent {

  @Input() isAuthenticated: boolean = true;
  @Input() role: string = '';
  @Input() appTitle: string = 'My App';

  isOpen: boolean = false;

  toggleDropdown() {
    this.isOpen = !this.isOpen;
  }

  logout() {
    console.log('Logout clicked');
  }

  openLoginModal() {
    console.log('Open login modal');
  }

  toggleMobileMenu() {
    console.log('Mobile menu');
  }
}
