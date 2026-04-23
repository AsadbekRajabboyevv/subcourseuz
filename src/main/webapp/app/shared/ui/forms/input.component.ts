import {
  Component, Input, forwardRef, CUSTOM_ELEMENTS_SCHEMA,
  OnDestroy, AfterViewInit, ElementRef, ViewChild, ChangeDetectorRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
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
  template: `
    <div class="space-y-2 w-full" [class.opacity-50]="disabled" [class.pointer-events-none]="disabled">
      <label *ngIf="label" class="field-label">{{ label }}</label>

      <div class="relative group">

        <ng-container *ngIf="['text', 'number', 'date'].includes(type)">
          <input [type]="type" [value]="value" [disabled]="disabled"
                 (input)="onValueChange($any($event.target).value)"
                 [placeholder]="placeholder" class="base-input">
        </ng-container>

        <ng-container *ngIf="type === 'textarea'">
          <textarea [value]="value" [disabled]="disabled"
                    (input)="onValueChange($any($event.target).value)"
                    [placeholder]="placeholder" rows="4" class="base-input"></textarea>
        </ng-container>

        <ng-container *ngIf="type === 'select'">
          <div class="select-wrapper">
            <select [ngModel]="value" [disabled]="disabled"
                    (ngModelChange)="onValueChange($event)" class="base-input base-select">
              <option value="" disabled>{{ placeholder }}</option>
              <option *ngFor="let opt of options" [value]="opt.value">{{ opt.label }}</option>
            </select>
          </div>
        </ng-container>

        <div *ngIf="type === 'multi-select'" class="multi-wrap">
          <button *ngFor="let opt of options" type="button" [disabled]="disabled"
                  (click)="toggleMultiSelect(opt.value)"
                  [class.multi-selected]="isSelected(opt.value)"
                  class="multi-btn">
            {{ opt.label }}
          </button>
        </div>

        <div *ngIf="type === 'checkbox'" (click)="!disabled && onValueChange(!value)"
             class="check-row" [class.checked-row]="value">
          <div class="check-box" [class.checked-box]="value">
            <svg *ngIf="value" width="11" height="11" viewBox="0 0 11 11" fill="none">
              <path d="M2 5.5L4.5 8L9 3" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <span class="check-label">{{ placeholder }}</span>
        </div>

        <div *ngIf="type === 'file'" class="file-zone">
          <input type="file" [disabled]="disabled" (change)="handleFileUpload($event)" class="file-input">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" class="file-icon">
            <path d="M12 16V8M12 8L9 11M12 8L15 11" stroke="#10b981" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <rect x="3" y="3" width="18" height="18" rx="6" stroke="#10b981" stroke-width="1.5" stroke-dasharray="3 2"/>
          </svg>
          <p class="file-text">{{ fileName || placeholder }}</p>
          <div *ngIf="uploadProgress > 0 && uploadProgress < 100" class="progress-track">
            <div class="progress-fill" [style.width.%]="uploadProgress"></div>
          </div>
        </div>

        <ng-container *ngIf="type === 'trix' || type === 'markdown'">
          <app-markdown-editor
            [placeholder]="placeholder"
            [disabled]="disabled"
            [ngModel]="value"
            (ngModelChange)="onValueChange($event)">
          </app-markdown-editor>
        </ng-container>

      </div>
    </div>
  `,
  styles: [`
    :host { display: block; }

    /* ─── Label ─── */
    .field-label {
      display: block;
      font-size: 10px; font-weight: 700;
      letter-spacing: 0.1em; text-transform: uppercase;
      color: #64748b; padding-left: 4px; margin-bottom: 6px;
    }
    :host-context(.dark) .field-label { color: #475569; }

    /* ─── Base input / textarea / select ─── */
    .base-input {
      width: 100%;
      border-radius: 14px;
      border: 2px solid #f1f5f9;
      background: #f1f5f9;
      padding: 12px 18px;
      font-size: 13px; font-weight: 600; color: #0f172a;
      outline: none;
      transition: border-color .2s, background .2s, box-shadow .2s;
      font-family: inherit;
      display: block;
    }
    .base-input::placeholder { color: #94a3b8; font-weight: 500; }
    .base-input:hover:not(:disabled) { border-color: #10b981; }
    .base-input:focus:not(:disabled) {
      border-color: #10b981; background: #fff;
      box-shadow: 0 0 0 4px rgba(16,185,129,.1);
    }
    .base-input:disabled { opacity: .45; cursor: not-allowed; }

    :host-context(.dark) .base-input {
      background: #1e293b; border-color: #1e293b; color: #f1f5f9;
    }
    :host-context(.dark) .base-input::placeholder { color: #475569; }
    :host-context(.dark) .base-input:focus:not(:disabled) {
      border-color: #10b981; background: #0f172a;
      box-shadow: 0 0 0 4px rgba(16,185,129,.12);
    }

    /* ─── Select ─── */
    .select-wrapper { position: relative; }
    .base-select { appearance: none; cursor: pointer; padding-right: 40px; }
    .select-wrapper::after {
      content: '▾'; position: absolute; right: 16px; top: 50%;
      transform: translateY(-50%); pointer-events: none;
      font-size: 12px; color: #94a3b8;
    }
    :host-context(.dark) .select-wrapper::after { color: #475569; }

    /* ─── Multi-select ─── */
    .multi-wrap { display: flex; flex-wrap: wrap; gap: 6px; }
    .multi-btn {
      padding: 6px 14px; border-radius: 10px; border: 2px solid #f1f5f9;
      background: #f1f5f9; color: #64748b;
      font-size: 10px; font-weight: 700; letter-spacing: .06em; text-transform: uppercase;
      cursor: pointer; transition: all .18s;
    }
    .multi-btn:hover { border-color: #6ee7b7; color: #059669; background: #f0fdf4; }
    .multi-btn.multi-selected { background: #ecfdf5; color: #059669; border-color: #6ee7b7; }
    :host-context(.dark) .multi-btn { background: #1e293b; border-color: #1e293b; color: #475569; }
    :host-context(.dark) .multi-btn:hover { border-color: #065f46; color: #34d399; background: #064e3b; }
    :host-context(.dark) .multi-btn.multi-selected { background: #064e3b; color: #34d399; border-color: #065f46; }

    /* ─── Checkbox ─── */
    .check-row {
      display: flex; align-items: center; gap: 12px;
      padding: 12px 16px; border-radius: 14px; border: 2px solid #f1f5f9;
      background: #f1f5f9; cursor: pointer; transition: all .18s; user-select: none;
    }
    .check-row:hover { border-color: #10b981; }
    .check-row.checked-row { border-color: #6ee7b7; background: #f0fdf4; }
    :host-context(.dark) .check-row { background: #1e293b; border-color: #1e293b; }
    :host-context(.dark) .check-row:hover { border-color: #10b981; }
    :host-context(.dark) .check-row.checked-row { border-color: #065f46; background: #064e3b22; }
    .check-box {
      width: 22px; height: 22px; border-radius: 7px; border: 2px solid #cbd5e1;
      background: #fff; display: flex; align-items: center; justify-content: center;
      flex-shrink: 0; transition: all .18s;
    }
    .check-box.checked-box { background: #10b981 !important; border-color: #10b981 !important; }
    :host-context(.dark) .check-box { border-color: #334155; background: #0f172a; }
    .check-label { font-size: 13px; font-weight: 600; color: #334155; }
    :host-context(.dark) .check-label { color: #cbd5e1; }

    /* ─── File ─── */
    .file-zone {
      border-radius: 14px; border: 2px dashed #cbd5e1; background: #f8fafc;
      padding: 24px 16px; text-align: center; cursor: pointer;
      transition: all .2s; position: relative;
    }
    .file-zone:hover { border-color: #10b981; background: #f0fdf4; }
    :host-context(.dark) .file-zone { border-color: #1e293b; background: #0f172a; }
    :host-context(.dark) .file-zone:hover { border-color: #10b981; background: rgba(6,78,59,.15); }
    .file-input { position: absolute; inset: 0; opacity: 0; cursor: pointer; z-index: 2; }
    .file-icon { margin: 0 auto 8px; display: block; }
    .file-text { font-size: 11px; font-weight: 700; letter-spacing: .06em; text-transform: uppercase; color: #64748b; }
    :host-context(.dark) .file-text { color: #475569; }
    .progress-track { height: 3px; border-radius: 2px; background: #e2e8f0; margin-top: 10px; overflow: hidden; }
    .progress-fill { height: 100%; background: #10b981; border-radius: 2px; transition: width .3s; }

    /* ─── Trix wrapper ─── */
    .trix-wrapper {
      border-radius: 16px; border: 2px solid #f1f5f9; overflow: hidden;
      background: #f1f5f9; transition: border-color .2s, background .2s, box-shadow .2s;
    }
    .trix-wrapper:hover { border-color: #10b981; }
    .trix-wrapper:focus-within {
      border-color: #10b981; background: #fff;
      box-shadow: 0 0 0 4px rgba(16,185,129,.1);
    }
    :host-context(.dark) .trix-wrapper { border-color: #1e293b; background: #1e293b; }
    :host-context(.dark) .trix-wrapper:hover { border-color: #10b981; }
    :host-context(.dark) .trix-wrapper:focus-within {
      border-color: #10b981; background: #0f172a;
      box-shadow: 0 0 0 4px rgba(16,185,129,.12);
    }

    /* ─── Custom toolbar (jadval tugmasi) ─── */
    .custom-toolbar {
      display: flex; align-items: center; gap: 4px;
      padding: 6px 10px; border-bottom: 1.5px solid #e2e8f0;
      background: rgba(255,255,255,.7); position: relative;
    }
    :host-context(.dark) .custom-toolbar {
      border-color: #0f172a; background: rgba(15,23,42,.6);
    }
    .custom-btn {
      display: inline-flex; align-items: center; gap: 5px;
      padding: 4px 10px; border-radius: 7px; border: none;
      background: transparent; color: #475569;
      font-size: 11px; font-weight: 700; cursor: pointer; transition: all .15s;
    }
    .custom-btn:hover { background: #ecfdf5; color: #059669; }
    :host-context(.dark) .custom-btn { color: #64748b; }
    :host-context(.dark) .custom-btn:hover { background: #064e3b; color: #34d399; }

    /* ─── Table picker ─── */
    .table-picker {
      position: absolute; top: calc(100% + 6px); left: 8px; z-index: 100;
      background: #fff; border: 1px solid #e2e8f0; border-radius: 12px;
      padding: 10px; box-shadow: 0 4px 20px rgba(0,0,0,.1);
    }
    :host-context(.dark) .table-picker {
      background: #1e293b; border-color: #334155;
      box-shadow: 0 4px 20px rgba(0,0,0,.4);
    }
    .picker-header {
      font-size: 11px; font-weight: 700; color: #10b981;
      text-align: center; margin-bottom: 8px; letter-spacing: .05em;
    }
    .picker-grid { display: grid; grid-template-columns: repeat(6, 20px); gap: 3px; }
    .picker-cell {
      width: 20px; height: 20px; border-radius: 4px; border: 1.5px solid #e2e8f0;
      background: #f8fafc; cursor: pointer; transition: all .1s;
    }
    :host-context(.dark) .picker-cell { border-color: #334155; background: #0f172a; }
    .picker-cell.picker-active { background: #ecfdf5; border-color: #10b981; }
    :host-context(.dark) .picker-cell.picker-active { background: #064e3b; border-color: #10b981; }

    /* ─── Trix Toolbar stilllari ─── */
    trix-toolbar {
      border-bottom: 1.5px solid #e2e8f0 !important;
      background: rgba(255,255,255,.7) !important;
      padding: 6px 10px !important;
    }
    :host-context(.dark) trix-toolbar {
      border-color: #0f172a !important;
      background: rgba(15,23,42,.6) !important;
    }
    trix-toolbar .trix-button-row {
      display: flex !important; flex-wrap: wrap !important; gap: 3px !important;
    }
    trix-toolbar .trix-button {
      border: none !important; background: transparent !important;
      border-radius: 7px !important; padding: 4px 8px !important;
      font-size: 11px !important; font-weight: 700 !important;
      color: #475569 !important; transition: all .15s !important;
    }
    :host-context(.dark) trix-toolbar .trix-button { color: #64748b !important; }
    trix-toolbar .trix-button:hover { background: #ecfdf5 !important; color: #059669 !important; }
    :host-context(.dark) trix-toolbar .trix-button:hover { background: #064e3b !important; color: #34d399 !important; }
    trix-toolbar .trix-button--active { background: #10b981 !important; color: #fff !important; }

    /* ─── Trix editor body ─── */
    trix-editor {
      min-height: 160px !important; border: none !important; outline: none !important;
      padding: 14px 18px !important; font-size: 13px !important;
      font-weight: 500 !important; line-height: 1.7 !important; color: #0f172a !important;
    }
    :host-context(.dark) trix-editor { color: #f1f5f9 !important; }

    /* ─── TRIX CONTENT: Bullets, Numbers, Heading, Quote, Code, Table ─── */

    /* Bullets - eng asosiy fix */
    trix-editor ul,
    .trix-content ul {
      list-style-type: disc !important;
      padding-left: 1.5em !important;
      margin: 0.3em 0 !important;
    }
    trix-editor ul ul,
    .trix-content ul ul { list-style-type: circle !important; }
    trix-editor ul ul ul,
    .trix-content ul ul ul { list-style-type: square !important; }

    /* Numbers - eng asosiy fix */
    trix-editor ol,
    .trix-content ol {
      list-style-type: decimal !important;
      padding-left: 1.5em !important;
      margin: 0.3em 0 !important;
    }

    /* li - display: list-item majburiy */
    trix-editor li,
    .trix-content li {
      display: list-item !important;
      margin: 0.1em 0 !important;
      padding-left: 0.2em !important;
    }

    /* Heading */
    trix-editor h1,
    .trix-content h1 {
      font-size: 1.2em !important; font-weight: 700 !important;
      color: inherit !important; margin: 0.6em 0 0.2em !important;
      line-height: 1.3 !important;
    }

    /* Blockquote */
    trix-editor blockquote,
    .trix-content blockquote {
      border-left: 3px solid #10b981 !important;
      border-radius: 0 !important;
      margin: 0.5em 0 !important;
      padding: 4px 0 4px 14px !important;
      color: #64748b !important; font-style: italic !important;
    }
    :host-context(.dark) trix-editor blockquote,
    :host-context(.dark) .trix-content blockquote { color: #475569 !important; }

    /* Code (inline pre) */
    trix-editor pre,
    .trix-content pre {
      background: #f1f5f9 !important; border-radius: 8px !important;
      padding: 10px 14px !important; font-family: monospace !important;
      font-size: 12px !important; font-weight: 600 !important;
      color: #0f172a !important; margin: 0.4em 0 !important;
      white-space: pre-wrap !important; word-break: break-all !important;
    }
    :host-context(.dark) trix-editor pre,
    :host-context(.dark) .trix-content pre {
      background: #0f172a !important; color: #f1f5f9 !important;
    }

    /* ─── Jadval stillari ─── */
    trix-editor table,
    .trix-content table {
      border-collapse: collapse !important; width: 100% !important;
      margin: 0.6em 0 !important; font-size: 12px !important;
      border-radius: 8px !important; overflow: hidden !important;
    }
    trix-editor table th,
    .trix-content table th {
      background: #f0fdf4 !important; color: #059669 !important;
      font-weight: 700 !important; padding: 8px 12px !important;
      border: 1px solid #d1fae5 !important; text-align: left !important;
    }
    :host-context(.dark) trix-editor table th,
    :host-context(.dark) .trix-content table th {
      background: #064e3b !important; color: #34d399 !important; border-color: #065f46 !important;
    }
    trix-editor table td,
    .trix-content table td {
      padding: 7px 12px !important; border: 1px solid #e2e8f0 !important;
      color: inherit !important; vertical-align: top !important;
    }
    :host-context(.dark) trix-editor table td,
    :host-context(.dark) .trix-content table td { border-color: #1e293b !important; }
    trix-editor table tr:nth-child(even) td,
    .trix-content table tr:nth-child(even) td { background: rgba(16,185,129,.04) !important; }
  `]
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

    trixEl.addEventListener('trix-initialize', initHandler, { once: true });
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

  /* ─── Jadval picker ─── */
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

    // Build table HTML
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

    // Trix insertHTML does not support <table> — inject directly into the editor element
    const editorBody = trixEl as HTMLElement;
    const currentHtml = editorBody.innerHTML || '';

    // Append table before the last empty <br> or at the end
    const tableWrapper = `<div class="trix-table-block">${tableHtml}</div><div><br></div>`;
    editorBody.innerHTML = currentHtml + tableWrapper;

    // Sync value back: read from the hidden input which Trix keeps in sync,
    // or fall back to innerHTML
    const hiddenInput = document.getElementById(this.trixInputId) as HTMLInputElement | null;
    const newValue = hiddenInput ? hiddenInput.value : editorBody.innerHTML;
    this.value = newValue;
    this.onChange(newValue);
    this.onTouch();
    this.cdr.markForCheck();
  }

  /* ─── ControlValueAccessor ─── */
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
    // If content contains a <table>, inject directly via innerHTML
    // because Trix's loadHTML strips table elements from its document model
    if (html && html.includes('<table')) {
      trixEl.innerHTML = html;
    } else {
      (trixEl as any).editor?.loadHTML(html ?? '');
    }
  }

  setDisabledState(isDisabled: boolean): void { this.disabled = isDisabled; }

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

  registerOnChange(fn: any): void { this.onChange = fn; }
  registerOnTouched(fn: any): void { this.onTouch = fn; }
}
