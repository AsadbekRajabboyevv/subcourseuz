import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgIconsModule, provideIcons } from '@ng-icons/core';
import { heroAcademicCap, heroBookOpen, heroPencil, heroSquares2x2, heroTrash } from '@ng-icons/heroicons/outline';

@Component({
  selector: 'app-course-card-table',
  standalone: true,
  imports: [CommonModule, NgIconsModule],
  providers: [provideIcons({ heroBookOpen, heroPencil, heroTrash, heroAcademicCap, heroSquares2x2 })],
  templateUrl: './course-card-table.component.html'
})
export class CourseCardTableComponent {
  @Input() courses: any[] = [];
  @Input() isLoading = false;

  @Output() edit = new EventEmitter<number>();
  @Output() delete = new EventEmitter<number>();

  onEdit(id: number) {
    this.edit.emit(id);
  }

  onDelete(id: number) {
    this.delete.emit(id);
  }
}
