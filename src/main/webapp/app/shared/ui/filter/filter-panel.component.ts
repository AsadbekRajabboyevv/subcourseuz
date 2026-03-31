import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FilterConfig, FilterQuery } from '../interfaces';
import { SearchInputComponent } from './search-input.component';
import { DatepickerComponent } from '../forms/datepicker/datepicker.component';
import { SelectComponent } from '../forms/select/select.component';

@Component({
  selector: 'app-filter-panel',
  standalone: true,
  imports: [CommonModule, SearchInputComponent, DatepickerComponent, SelectComponent],
  templateUrl: './filter-panel.component.html',
})
export class FilterPanelComponent {
  @Input() config: FilterConfig[] = [];

  @Output() filterChange = new EventEmitter<FilterQuery>();

  protected query: FilterQuery = {};

  protected onSearchChange(value: string): void {
    this.query = { ...this.query, search: value };
    this.emitChange();
  }

  protected onFieldChange(key: string, value: unknown): void {
    this.query = { ...this.query, [key]: value };
    this.emitChange();
  }

  protected onDateRangeChange(
    key: string,
    which: 'from' | 'to',
    value: unknown,
  ): void {
    const existing = (this.query[key] as { from?: unknown; to?: unknown }) ?? {};
    const updated = { ...existing, [which]: value };
    this.query = { ...this.query, [key]: updated };
    this.emitChange();
  }

  reset(): void {
    this.query = {};
    this.emitChange();
  }

  private emitChange(): void {
    this.filterChange.emit({ ...this.query });
  }
}

