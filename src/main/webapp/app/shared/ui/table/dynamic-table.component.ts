import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output, computed, inject, signal } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { ColumnConfig, FilterQuery, SortEvent } from '../interfaces';

@Component({
  selector: 'app-dynamic-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dynamic-table.component.html',
})
export class DynamicTableComponent<T extends Record<string, unknown>> {
  @Input() columns: ColumnConfig[] = [];
  @Input() data: T[] = [];
  @Input() totalCount = 0;
  @Input() pageSize = 10;
  @Input() loading = false;

  @Output() sortChange = new EventEmitter<SortEvent>();
  @Output() pageChange = new EventEmitter<{ pageIndex: number; pageSize: number }>();
  @Output() filterChange = new EventEmitter<FilterQuery>();

  protected currentPage = signal(0);
  protected currentSort = signal<SortEvent | null>(null);

  private readonly authService = inject(AuthService);

  protected visibleColumns = computed(() => {
    const role = this.authService.currentRole();
    return this.columns.filter(col => {
      if (!col.roles || col.roles.length === 0) return true;
      if (!role) return false;
      return col.roles.includes(role);
    });
  });

  protected totalPages = computed(() => {
    if (this.pageSize <= 0) return 0;
    return Math.ceil(this.totalCount / this.pageSize);
  });

  protected onHeaderClick(column: ColumnConfig): void {
    if (!column.sortable) return;

    const current = this.currentSort();
    let direction: SortEvent['direction'] = 'asc';

    if (current && current.field === column.field) {
      direction = current.direction === 'asc' ? 'desc' : 'asc';
    }

    const sortEvent: SortEvent = { field: column.field, direction };
    this.currentSort.set(sortEvent);
    this.sortChange.emit(sortEvent);
  }

  protected isSortedAsc(column: ColumnConfig): boolean {
    const sort = this.currentSort();
    return !!sort && sort.field === column.field && sort.direction === 'asc';
  }

  protected isSortedDesc(column: ColumnConfig): boolean {
    const sort = this.currentSort();
    return !!sort && sort.field === column.field && sort.direction === 'desc';
  }

  protected goToPage(delta: number): void {
    const next = this.currentPage() + delta;
    const lastIndex = Math.max(this.totalPages() - 1, 0);
    const clamped = Math.min(Math.max(next, 0), lastIndex);
    if (clamped === this.currentPage()) return;

    this.currentPage.set(clamped);
    this.pageChange.emit({ pageIndex: clamped, pageSize: this.pageSize });
  }

  protected trackByRow(_index: number, row: T): unknown {
    return row;
  }

  protected trackByColumn(_index: number, column: ColumnConfig): string {
    return column.field;
  }

  protected onFilterChange(query: FilterQuery): void {
    this.filterChange.emit(query);
  }
}

