import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { BreadcrumbService } from '../services/breadcrumb.service';

@Component({
  selector: 'app-breadcrumb',
  standalone: true,
  imports: [CommonModule, RouterModule, LucideAngularModule],
  templateUrl: './breadcrumb.component.html'
})
export class BreadcrumbComponent {
  private bcService = inject(BreadcrumbService);
  breadcrumbs = this.bcService.breadcrumbs;
}
