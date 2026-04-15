import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ErrorService} from "./error.service";

@Component({
  selector: 'app-error-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './error-modal.component.html',
  styles: [`
    :host {
      display: block;
      position: relative;
    }
  `]
})
export class ErrorModalComponent {
  protected errorService = inject(ErrorService);


  close(): void {
    this.errorService.close();
  }

  onBackdropClick(event: MouseEvent): void {
    if ((event.target as HTMLElement).classList.contains('backdrop-blur-md')) {
      this.close();
    }
  }
}
