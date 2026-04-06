import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-course-card-table',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
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
