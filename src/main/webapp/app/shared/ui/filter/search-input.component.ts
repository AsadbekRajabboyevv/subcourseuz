import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject, Subscription, debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
  selector: 'app-search-input',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './search-input.component.html',
})
export class SearchInputComponent implements OnInit, OnDestroy {
  @Input() placeholder = 'Qidirish...';
  @Input() debounceMs = 300;

  @Output() searchChange = new EventEmitter<string>();

  protected value = '';

  private input$ = new Subject<string>();
  private sub?: Subscription;

  ngOnInit(): void {
    this.sub = this.input$
      .pipe(debounceTime(this.debounceMs), distinctUntilChanged())
      .subscribe(value => this.searchChange.emit(value));
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  protected onInput(value: string): void {
    this.value = value;
    this.input$.next(value);
  }

  protected readonly HTMLInputElement = HTMLInputElement;
}

