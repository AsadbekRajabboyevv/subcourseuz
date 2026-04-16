import { Component } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-error-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './error.component.html'
})
export class ErrorPageComponent {
  constructor(private location: Location) {}

  goBack(): void {
    this.location.back();
  }
}
