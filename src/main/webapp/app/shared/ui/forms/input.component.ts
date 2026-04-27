import {
  Component, Input, forwardRef, CUSTOM_ELEMENTS_SCHEMA,
  OnDestroy, AfterViewInit, ElementRef, ViewChild, ChangeDetectorRef
} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor} from '@angular/forms';
import {MarkdownEditorComponent} from "./markdown-editor/markdown-editor.component";

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [CommonModule, FormsModule, MarkdownEditorComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => InputComponent),
    multi: true
  }],
  templateUrl: './input.component.html',
  styleUrl: './input.component.scss'
})
export class InputComponent implements ControlValueAccessor, AfterViewInit, OnDestroy {
  @Input() type: 'text' | 'number' | 'select' | 'multi-select' | 'checkbox' | 'date' | 'textarea' | 'file' | 'trix' | 'markdown' = 'text';
  @Input() label: string = '';
  @Input() placeholder: string = 'Tanlang';
  @Input() options: { label: string, value: any }[] = [];
  @Input() disabled: boolean = false;

  @ViewChild('trixEditor') trixEditorRef!: ElementRef;

  readonly trixInputId = `trix-input-${Math.random().toString(36).slice(2, 9)}`;

  value: any = '';
  uploadProgress = 0;
  fileName = '';

  showTablePicker = false;
  tableRows = 3;
  tableCols = 3;
  tablePickerRows = Array(6).fill(0);
  tablePickerCols = Array(6).fill(0);

  private pendingValue: string | null = null;
  private trixReady = false;
  private trixChangeHandler!: (e: Event) => void;
  private outsideClickHandler!: (e: MouseEvent) => void;

  onChange: any = () => {};
  onTouch: any = () => {};

  constructor(private cdr: ChangeDetectorRef) {}

  ngAfterViewInit(): void {
    if (this.type !== 'trix') return;
    this._initTrix();
  }

  private _initTrix(): void {
    const trixEl = this.trixEditorRef?.nativeElement;
    if (!trixEl) return;

    const initHandler = () => {
      this.trixReady = true;
      if (this.pendingValue !== null) {
        this._loadHtmlIntoTrix(trixEl, this.pendingValue);
        this.pendingValue = null;
      }
    };

    this.trixChangeHandler = () => {
      const newValue = trixEl.value;
      if (this.value !== newValue) {
        this.value = newValue;
        this.onChange(newValue);
        this.onTouch();
        this.cdr.markForCheck();
      }
    };

    trixEl.addEventListener('trix-initialize', initHandler, {once: true});
    trixEl.addEventListener('trix-change', this.trixChangeHandler);

    this.outsideClickHandler = (e: MouseEvent) => {
      if (!(e.target as HTMLElement).closest('.custom-toolbar')) {
        this.showTablePicker = false;
        this.cdr.markForCheck();
      }
    };
    document.addEventListener('click', this.outsideClickHandler);
  }

  ngOnDestroy(): void {
    if (this.type !== 'trix') return;
    const trixEl = this.trixEditorRef?.nativeElement;
    if (trixEl && this.trixChangeHandler) {
      trixEl.removeEventListener('trix-change', this.trixChangeHandler);
    }
    document.removeEventListener('click', this.outsideClickHandler);
  }

  hoverCell(row: number, col: number): void {
    this.tableRows = row;
    this.tableCols = col;
  }

  insertTable(): void {
    this.showTablePicker = !this.showTablePicker;
    this.tableRows = 3;
    this.tableCols = 3;
  }

  confirmTable(): void {
    this.showTablePicker = false;
    const trixEl = this.trixEditorRef?.nativeElement;
    if (!trixEl || !this.trixReady) return;

    const rows = this.tableRows;
    const cols = this.tableCols;

    let tableHtml = '<table><thead><tr>';
    for (let c = 0; c < cols; c++) {
      tableHtml += `<th>Ustun ${c + 1}</th>`;
    }
    tableHtml += '</tr></thead><tbody>';
    for (let r = 0; r < rows; r++) {
      tableHtml += '<tr>';
      for (let c = 0; c < cols; c++) {
        tableHtml += `<td>${r + 1}</td>`;
      }
      tableHtml += '</tr>';
    }
    tableHtml += '</tbody></table>';

    const editorBody = trixEl as HTMLElement;
    const currentHtml = editorBody.innerHTML || '';

    const tableWrapper = `<div class="trix-table-block">${tableHtml}</div><div><br></div>`;
    editorBody.innerHTML = currentHtml + tableWrapper;

    const hiddenInput = document.getElementById(this.trixInputId) as HTMLInputElement | null;
    const newValue = hiddenInput ? hiddenInput.value : editorBody.innerHTML;
    this.value = newValue;
    this.onChange(newValue);
    this.onTouch();
    this.cdr.markForCheck();
  }

  writeValue(value: any): void {
    this.value = value ?? '';
    if (this.type !== 'trix') return;
    const trixEl = this.trixEditorRef?.nativeElement;
    if (trixEl && this.trixReady) {
      this._loadHtmlIntoTrix(trixEl, this.value);
    } else {
      this.pendingValue = this.value;
    }
  }

  private _loadHtmlIntoTrix(trixEl: HTMLElement, html: string): void {
    if (html && html.includes('<table')) {
      trixEl.innerHTML = html;
    } else {
      (trixEl as any).editor?.loadHTML(html ?? '');
    }
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onValueChange(newValue: any): void {
    if (this.disabled) return;
    this.value = newValue;
    this.onChange(newValue);
    this.onTouch();
  }

  handleFileUpload(event: any): void {
    const file = event.target.files[0];
    if (!file) return;
    this.fileName = file.name;
    this.uploadProgress = 0;
    const interval = setInterval(() => {
      this.uploadProgress += 10;
      if (this.uploadProgress >= 100) {
        clearInterval(interval);
        this.onValueChange(file);
      }
    }, 100);
  }

  toggleMultiSelect(optValue: any): void {
    if (!Array.isArray(this.value)) this.value = [];
    const i = this.value.indexOf(optValue);
    i > -1 ? this.value.splice(i, 1) : this.value.push(optValue);
    this.onChange([...this.value]);
    this.onTouch();
  }

  isSelected(optValue: any): boolean {
    return Array.isArray(this.value) && this.value.includes(optValue);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouch = fn;
  }
}
