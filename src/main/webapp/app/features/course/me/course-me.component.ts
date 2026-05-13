import { Component, OnInit, inject, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CourseService } from "../course.service";
import { InputComponent } from "../../../shared/ui/forms/input.component";
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";
import { CourseFilter, DurationType } from "../course.model";
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-course-me',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    InputComponent,
    PageWrapperComponent,
    RouterLink,
  ],
  templateUrl: './course-me.component.html'
})
export class CourseMeComponent implements OnInit {
  public courseService = inject(CourseService);

  page = 0;
  size = 12;
  hasMore = true;
  isFilterVisible = false;
  filter: CourseFilter = {
    search: '',
    isPublished: true,
    scienceId: undefined,
    gradeId: undefined,
    priceFrom: undefined,
    priceTo: undefined,
    lang: '',
    durationType: undefined,
    duration: undefined,
  };

  durationTypes = Object.entries(DurationType).map(([key, value]) => ({
    label: key,
    value: value
  }));

  languages = [
    { label: 'O\'zbekcha', value: 'UZ' },
    { label: 'Ruscha', value: 'RU' },
    { label: 'Inglizcha', value: 'EN' }
  ];

  ngOnInit() {
    this.loadData();
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const threshold = 150;
    const position = window.innerHeight + window.scrollY;
    const height = document.documentElement.scrollHeight;

    if (height - position < threshold && !this.courseService.isLoading() && this.hasMore) {
      this.loadData();
    }
  }

  loadData() {
    if (!this.hasMore || this.courseService.isLoading()) return;

    this.courseService.getMe(this.filter, this.page, this.size).subscribe({
      next: (res) => {
        const newData = res.data.content;

        if (this.page === 0) {
          this.courseService.setCourses(newData);
        } else {
          this.courseService.appendCourses(newData);
        }

        this.hasMore = !res.data.last;
        this.page++;
      }
    });
  }

  applyFilters() {
    this.page = 0;
    this.hasMore = true;
    this.loadData();
  }

  resetFilters() {
    this.filter = {
      search: '',
      isPublished: true
    };
    this.applyFilters();
  }
}
