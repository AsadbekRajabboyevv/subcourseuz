import { CommonModule } from '@angular/common';
import {Component, OnDestroy, OnInit, computed, inject, signal, EffectRef} from '@angular/core';
import { RouterModule } from '@angular/router';
import { MenuItem } from '../interfaces';
import { MenuService } from '../services/menu.service';
import { AuthService } from '../services/auth.service';
import { Subscription } from 'rxjs';
import { effect } from '@angular/core';

@Component({
  selector: 'app-sidebar-menu',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar-menu.component.html',
})
export class SidebarMenuComponent implements OnInit, OnDestroy {
  private readonly menuService = inject(MenuService);
  private readonly authService = inject(AuthService);

  protected items = signal<MenuItem[]>([]);
  protected expanded = signal<Record<string, boolean>>({});
  private effectRef!: EffectRef;
  private sub?: Subscription;

  private readonly role = computed(() => this.authService.currentRole());

  ngOnInit(): void {
    this.loadMenu();

    this.effectRef = effect(() => {
      const user = this.authService.currentUser();
      this.loadMenu();
    });
  }

  ngOnDestroy(): void {
    this.effectRef.destroy();
  }

  protected toggle(item: MenuItem): void {
    if (!item.children || item.children.length === 0) return;
    this.expanded.update(state => ({ ...state, [item.id]: !state[item.id] }));
  }

  protected isExpanded(item: MenuItem): boolean {
    return !!this.expanded()[item.id];
  }

  private loadMenu(): void {
    const role = this.role();
    if (!role) {
      this.items.set([]);
      return;
    }

    this.menuService.getMenuItems(role).subscribe(items => {
      const filtered = items.filter(i => !i.roles || i.roles.length === 0 || i.roles.includes(role));
      this.items.set(filtered);
    });
  }
}

