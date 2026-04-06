import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule } from 'lucide-angular';
import {PageWrapperComponent} from "../../../shared/ui";
import {CourseService} from "../service/course.service";
import {Course, CourseFilter} from "../model/course";
import {CourseCardTableComponent} from "./course-card-table.component";

@Component({
  selector: 'app-course',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    LucideAngularModule,
    CourseCardTableComponent,
    PageWrapperComponent
  ],
  templateUrl: './course.component.html'
})
export class CourseComponent implements OnInit {
  private courseService = inject(CourseService);
  courses = signal<Course[]>([]);
  totalElements = signal(0);
  isLoading = signal(false);
  currentPage = signal(0);
  pageSize = signal(10);

  // Qidiruv filtri
  filter: Partial<CourseFilter> = {
    name: '',
    lang: ''
  };

  ngOnInit(): void {
    this.loadCourses();
  }

  /**
   * Kurslarni yuklash mantiqi
   */
  loadCourses(): void {
    this.isLoading.set(true);

    this.courseService.getPaginated(
      this.filter,
      this.currentPage(),
      this.pageSize()
    ).subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.courses.set(res.data.content);
          this.totalElements.set(res.data.totalElements);
        }
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Kurslarni yuklashda xatolik:', err);
        this.isLoading.set(false);
      }
    });
  }

  /**
   * Qidiruv tugmasi bosilganda
   */
  onSearch(): void {
    this.currentPage.set(0); // Qidirganda har doim 1-sahifaga qaytish
    this.loadCourses();
  }

  /**
   * Sahifa o'zgarganda
   */
  onPageChange(newPage: number): void {
    this.currentPage.set(newPage);
    this.loadCourses();
  }

  /**
   * Kursni o'chirish
   */
  onDelete(id: number): void {
    if (confirm('Ushbu kursni o\'chirishni tasdiqlaysizmi?')) {
      this.courseService.delete(id).subscribe({
        next: () => {
          this.loadCourses();
          // Bu yerda Toast/Alert chiqarish mumkin
        }
      });
    }
  }

  /**
   * Tahrirlash sahifasiga o'tish (mantiqiy misol)
   */
  onEdit(id: number): void {
    console.log('Tahrirlash ID:', id);
    // this.router.navigate(['/courses/edit', id]);
  }
}
