import { CommonModule } from '@angular/common';
import { Component, Input, signal } from '@angular/core';

@Component({
  selector: 'app-sidebar-layout',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar-layout.component.html',
})
export class SidebarLayoutComponent {
  @Input() sidebarWidth = '64';
  @Input() collapsible = true;

  protected collapsed = signal(false);

  toggle(): void {
    if (!this.collapsible) return;
    this.collapsed.update(v => !v);
  }

  protected get desktopSidebarWidthClass(): string {
    return `md:w-${this.sidebarWidth}`;
  }
}

