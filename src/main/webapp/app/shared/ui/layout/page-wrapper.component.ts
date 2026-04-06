// page-wrapper.component.ts
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { BreadcrumbComponent } from '../breadcrumb/breadcrumb.component';

@Component({
  selector: 'app-page-wrapper',
  standalone: true,
  imports: [CommonModule, BreadcrumbComponent],
  templateUrl: './page-wrapper.component.html',
})
export class PageWrapperComponent {}
