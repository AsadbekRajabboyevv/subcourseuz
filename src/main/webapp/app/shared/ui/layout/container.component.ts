import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-container',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './container.component.html',
})
export class ContainerComponent {
  @Input() maxWidth: 'full' | '7xl' | '5xl' | '3xl' = '7xl';
  @Input() padding: '0' | '4' | '6' | '8' = '6';

  protected get containerClasses(): string {
    const widthClass =
      this.maxWidth === 'full'
        ? 'max-w-full'
        : this.maxWidth === '5xl'
        ? 'max-w-5xl'
        : this.maxWidth === '3xl'
        ? 'max-w-3xl'
        : 'max-w-7xl';

    const paddingClass =
      this.padding === '0'
        ? 'px-0'
        : this.padding === '4'
        ? 'px-4'
        : this.padding === '8'
        ? 'px-8'
        : 'px-6';

    return `${widthClass} mx-auto ${paddingClass}`;
  }
}

