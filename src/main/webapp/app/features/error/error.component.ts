import { Component } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterLink } from '@angular/router';
import { PageWrapperComponent } from '../../shared/ui/layout/page-wrapper.component';
import {GridComponent} from "../../shared/ui/layout/grid.component";

@Component({
  selector: 'app-error-page',
  standalone: true,
  imports: [CommonModule, RouterLink, PageWrapperComponent, GridComponent],
  templateUrl: './error.component.html'
})
export class ErrorPageComponent {
  constructor(private location: Location) {}

  goBack(): void {
    this.location.back();
  }
}
