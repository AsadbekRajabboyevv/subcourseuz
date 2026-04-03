import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, computed, inject, signal, TemplateRef } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { ColumnConfig, SortEvent } from '../interfaces';
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-dynamic-table',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './dynamic-table.component.html',
})
export class DynamicTableComponent<T extends Record<string, any>> {
  @Input() columns: ColumnConfig[] = [];
  @Input() data: T[] = [];
  @Input() totalCount = 0;
  @Input() pageSize = 10;
  @Input() loading = false;
  @Input() showActions = true; // Amallar ustunini ko'rsatish/yashirish

  // Custom action template (agar foydalanuvchi o'z tugmalarini qo'shmoqchi bo'lsa)
  @Input() customActions?: TemplateRef<any>;

  @Output() sortChange = new EventEmitter<SortEvent>();
  @Output() pageChange = new EventEmitter<{ pageIndex: number; pageSize: number }>();
  @Output() edit = new EventEmitter<T>();
  @Output() delete = new EventEmitter<T>();
  @Output() toggleStatus = new EventEmitter<T>(); // Enable/Disable uchun

  protected currentPage = signal(0);
  protected currentSort = signal<SortEvent | null>(null);
  private readonly authService = inject(AuthService);

  // Rollarga qarab ustunlarni filtrlash
  protected visibleColumns = computed(() => {
    const role = this.authService.currentRole();
    return this.columns.filter(col => {
      if (!col.roles || col.roles.length === 0) return true;
      return role ? col.roles.includes(role) : false;
    });
  });

  protected totalPages = computed(() => Math.ceil(this.totalCount / this.pageSize) || 1);

  protected onHeaderClick(column: ColumnConfig): void {
    if (!column.sortable) return;
    const current = this.currentSort();
    const direction = current?.field === column.field && current.direction === 'asc' ? 'desc' : 'asc';
    const sortEvent: SortEvent = { field: column.field, direction };
    this.currentSort.set(sortEvent);
    this.sortChange.emit(sortEvent);
  }

  protected goToPage(delta: number): void {
    const next = Math.min(Math.max(this.currentPage() + delta, 0), this.totalPages() - 1);
    if (next !== this.currentPage()) {
      this.currentPage.set(next);
      this.pageChange.emit({ pageIndex: next, pageSize: this.pageSize });
    }
  }
}
