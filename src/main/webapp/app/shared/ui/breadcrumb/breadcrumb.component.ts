import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgIconsModule, provideIcons } from '@ng-icons/core';
import { heroChevronRight, heroHome } from '@ng-icons/heroicons/outline';
import { BreadcrumbService } from '../services/breadcrumb.service';

@Component({
  selector: 'app-breadcrumb',
  standalone: true,
  imports: [CommonModule, RouterModule, NgIconsModule],
  providers: [provideIcons({ heroHome, heroChevronRight })],
  templateUrl: './breadcrumb.component.html'
})
export class BreadcrumbComponent {
  private bcService = inject(BreadcrumbService);
  breadcrumbs = this.bcService.breadcrumbs;
}
