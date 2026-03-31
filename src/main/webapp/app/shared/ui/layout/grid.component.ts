import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-grid',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './grid.component.html',
})
export class GridComponent {
  @Input() cols = 3;
  @Input() gap = 4;
  @Input() responsive = true;

  protected get gridClasses(): string {
    const base = 'grid';
    const gapClass = `gap-${this.gap}`;

    if (!this.responsive) {
      return `${base} grid-cols-${this.cols} ${gapClass}`;
    }

    // mobile 1 col, md+ uses configured cols
    return `${base} grid-cols-1 md:grid-cols-${this.cols} ${gapClass}`;
  }
}

