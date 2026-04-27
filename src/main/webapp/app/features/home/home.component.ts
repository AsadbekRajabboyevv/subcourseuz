import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PageWrapperComponent } from '../../shared/ui/layout/page-wrapper.component';
import {TranslatePipe, TranslateModule} from "@ngx-translate/core";

interface Course {
  id: number;
  title: string;
  category: string;
  image: string;
  mentor: string;
  mentorImage: string;
  price: number;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, PageWrapperComponent, TranslatePipe, TranslateModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  currentSlideIndex = 0;
  private touchStartX = 0;
  private touchEndX = 0;

  courses: Course[] = [
    { id: 1, title: 'Java Backend Pro', category: 'Backend', image: 'https://picsum.photos/400/250?random=1', mentor: 'Asadbek R.', mentorImage: 'https://i.pravatar.cc/150?u=1', price: 1200000 },
    { id: 2, title: 'Angular Masterclass', category: 'Frontend', image: 'https://picsum.photos/400/250?random=2', mentor: 'Farhod A.', mentorImage: 'https://i.pravatar.cc/150?u=2', price: 900000 },
    { id: 3, title: 'Spring Boot & Kafka', category: 'Backend', image: 'https://picsum.photos/400/250?random=3', mentor: 'Asadbek R.', mentorImage: 'https://i.pravatar.cc/150?u=1', price: 1500000 },
    { id: 4, title: 'Python Data Science', category: 'Data', image: 'https://picsum.photos/400/250?random=4', mentor: 'Malika T.', mentorImage: 'https://i.pravatar.cc/150?u=3', price: 850000 },
    { id: 5, title: 'UI/UX Essentials', category: 'Design', image: 'https://picsum.photos/400/250?random=5', mentor: 'Sardor M.', mentorImage: 'https://i.pravatar.cc/150?u=4', price: 700000 },
    { id: 6, title: 'Flutter Mobile', category: 'Mobile', image: 'https://picsum.photos/400/250?random=6', mentor: 'Doston B.', mentorImage: 'https://i.pravatar.cc/150?u=5', price: 1100000 },
    { id: 7, title: 'DevOps Docker', category: 'DevOps', image: 'https://picsum.photos/400/250?random=7', mentor: 'Nodir X.', mentorImage: 'https://i.pravatar.cc/150?u=6', price: 1300000 },
  ];

  ngOnInit(): void {
    setInterval(() => {
      this.nextSlide();
    }, 5000);
  }

  nextSlide() {
    const visibleCards = 6;
    const totalCourses = this.courses.length;

    if (this.currentSlideIndex >= totalCourses - visibleCards) {
      this.currentSlideIndex = 0;
    } else {
      this.currentSlideIndex++;
    }
  }

  prevSlide() {
    const visibleCards = 6;
    const totalCourses = this.courses.length;

    if (this.currentSlideIndex <= 0) {
      this.currentSlideIndex = totalCourses - visibleCards;
    } else {
      this.currentSlideIndex--;
    }
  }

  onTouchStart(e: TouchEvent) { this.touchStartX = e.changedTouches[0].screenX; }
  onTouchEnd(e: TouchEvent) {
    this.touchEndX = e.changedTouches[0].screenX;
    if (this.touchStartX - this.touchEndX > 50) this.nextSlide();
    if (this.touchEndX - this.touchStartX > 50) this.prevSlide();
  }

}
