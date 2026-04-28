import {
  Component, forwardRef, Input, signal, CUSTOM_ELEMENTS_SCHEMA,
  ViewChild, ElementRef, AfterViewInit, OnDestroy, inject
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { MarkdownModule } from 'ngx-markdown';
import '@github/markdown-toolbar-element';
import { MarkdownFileUploadService } from './file-upload.service';

type Tab = 'write' | 'preview';

export interface UploadingFile {
  id: string;
  name: string;
  progress: number;
  error?: string;
  done: boolean;
}

@Component({
  selector: 'app-markdown-editor',
  standalone: true,
  imports: [CommonModule, FormsModule, MarkdownModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => MarkdownEditorComponent),
    multi: true
  }],
  templateUrl: './markdown-editor.component.html',
  styleUrls: ['./markdown-editor.component.scss']
})
export class MarkdownEditorComponent implements ControlValueAccessor, AfterViewInit, OnDestroy {
  @Input() placeholder = 'Leave a comment';
  @Input() disabled = false;
  @Input() minHeight = '200px';

  @ViewChild('textarea') textareaRef!: ElementRef<HTMLTextAreaElement>;
  @ViewChild('fileInput') fileInputRef!: ElementRef<HTMLInputElement>;

  readonly uploadService = inject(MarkdownFileUploadService);

  value = signal('');
  activeTab = signal<Tab>('write');
  isDragOver = signal(false);
  uploadingFiles = signal<UploadingFile[]>([]);

  private _onChange: (v: string) => void = () => {};
  private _onTouched: () => void = () => {};

  readonly _id = 'md-ta-' + Math.random().toString(36).slice(2, 7);

  ngAfterViewInit(): void {
    if (this.textareaRef?.nativeElement && this.value()) {
      this.textareaRef.nativeElement.value = this.value();
    }
  }

  ngOnDestroy(): void {}

  // ── ControlValueAccessor ──────────────────────────────────────────────────

  writeValue(val: string): void {
    this.value.set(val ?? '');
    if (this.textareaRef?.nativeElement) {
      this.textareaRef.nativeElement.value = val ?? '';
    }
  }

  registerOnChange(fn: (v: string) => void): void { this._onChange = fn; }
  registerOnTouched(fn: () => void): void { this._onTouched = fn; }
  setDisabledState(d: boolean): void { this.disabled = d; }

  // ── Textarea events ───────────────────────────────────────────────────────

  onInput(event: Event): void {
    const val = (event.target as HTMLTextAreaElement).value;
    this.value.set(val);
    this._onChange(val);
  }

  onBlur(): void { this._onTouched(); }

  setTab(tab: Tab): void {
    this.activeTab.set(tab);
    if (tab === 'write') {
      setTimeout(() => this.textareaRef?.nativeElement.focus(), 0);
    }
  }

  // ── Drag & Drop ───────────────────────────────────────────────────────────

  onDragOver(e: DragEvent): void {
    e.preventDefault();
    e.stopPropagation();
    this.isDragOver.set(true);
  }

  onDragLeave(e: DragEvent): void {
    e.preventDefault();
    this.isDragOver.set(false);
  }

  onDrop(e: DragEvent): void {
    e.preventDefault();
    e.stopPropagation();
    this.isDragOver.set(false);
    const files = Array.from(e.dataTransfer?.files ?? []);
    files.forEach(f => this.uploadFile(f));
  }

  onPaste(e: ClipboardEvent): void {
    const items = Array.from(e.clipboardData?.items ?? []);
    const imageItem = items.find(i => i.type.startsWith('image/'));
    if (imageItem) {
      e.preventDefault();
      const file = imageItem.getAsFile();
      if (file) this.uploadFile(file);
    }
  }

  // ── File input ────────────────────────────────────────────────────────────

  triggerFileInput(): void {
    this.fileInputRef?.nativeElement.click();
  }

  onFileInputChange(e: Event): void {
    const files = Array.from((e.target as HTMLInputElement).files ?? []);
    files.forEach(f => this.uploadFile(f));
    (e.target as HTMLInputElement).value = '';
  }

  // ── Upload ────────────────────────────────────────────────────────────────

  uploadFile(file: File): void {
    const error = this.uploadService.validate(file);
    const id = crypto.randomUUID();

    if (error) {
      this.uploadingFiles.update(list => [...list, { id, name: file.name, progress: 0, error, done: true }]);
      setTimeout(() => this.removeUploadItem(id), 4000);
      return;
    }

    // Insert placeholder into textarea
    const isImage = this.uploadService.isImage(file.type);
    const placeholder = isImage
      ? `![Uploading ${file.name}…]()`
      : `[Uploading ${file.name}…]()`;

    this.insertAtCursor(placeholder + '\n');
    this.uploadingFiles.update(list => [...list, { id, name: file.name, progress: 0, done: false }]);

    this.uploadService.upload(file).subscribe({
      next: ({ progress, url }) => {
        this.uploadingFiles.update(list =>
          list.map(f => f.id === id ? { ...f, progress } : f)
        );
        if (url) {
          // Replace placeholder with real URL
          const realMd = isImage
            ? `![${file.name}](${url})`
            : `[${file.name}](${url})`;
          const current = this.textareaRef.nativeElement.value;
          const updated = current.replace(placeholder, realMd);
          this.textareaRef.nativeElement.value = updated;
          this.value.set(updated);
          this._onChange(updated);
          this.uploadingFiles.update(list =>
            list.map(f => f.id === id ? { ...f, done: true, progress: 100 } : f)
          );
          setTimeout(() => this.removeUploadItem(id), 2000);
        }
      },
      error: () => {
        // Remove placeholder on error
        const current = this.textareaRef.nativeElement.value;
        const updated = current.replace(placeholder + '\n', '');
        this.textareaRef.nativeElement.value = updated;
        this.value.set(updated);
        this._onChange(updated);
        this.uploadingFiles.update(list =>
          list.map(f => f.id === id ? { ...f, error: 'Yuklashda xatolik', done: true } : f)
        );
        setTimeout(() => this.removeUploadItem(id), 4000);
      }
    });
  }

  private removeUploadItem(id: string): void {
    this.uploadingFiles.update(list => list.filter(f => f.id !== id));
  }

  // ── Cursor helpers ────────────────────────────────────────────────────────

  private insertAtCursor(text: string): void {
    const ta = this.textareaRef?.nativeElement;
    if (!ta) return;
    const start = ta.selectionStart;
    const end = ta.selectionEnd;
    const newVal = ta.value.substring(0, start) + text + ta.value.substring(end);
    ta.value = newVal;
    this.value.set(newVal);
    this._onChange(newVal);
    ta.focus();
    ta.setSelectionRange(start + text.length, start + text.length);
  }

  // ── Image resize helper (inserts with width) ──────────────────────────────

  insertImageWithSize(url: string, alt: string, width: number): void {
    // GitHub-style: use HTML img tag for resizing since markdown doesn't support it
    const html = `<img src="${url}" alt="${alt}" width="${width}">\n`;
    this.insertAtCursor(html);
  }
}
