import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { environment } from 'environments/environment';
import {
  AlertComponent, ConfirmModalComponent, ContainerComponent,
  DatepickerComponent, DynamicFormComponent,
  DynamicTableComponent, FileUploadComponent, FilterPanelComponent,
  InputComponent,
  PageWrapperComponent, SearchInputComponent, SelectComponent,
  ThemeSwitchComponent, ToastContainerComponent
} from "../../shared/ui";

@Component({
  selector: 'app-home',
  imports: [CommonModule, PageWrapperComponent, ThemeSwitchComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit, OnDestroy {
  environment = environment;

  currentSlideIndex = 0;
  private autoSlideInterval: any;

  // Kurslar ro'yxati
  courses = [
    {
      id: 1,
      title: 'Web Development',
      description: 'Zamonaviy web dasturlash asoslari',
      mentor: 'John Doe',
      mentorImage: 'https://picsum.photos/seed/mentor1/32/32.jpg',
      category: 'Yangi',
      categoryColor: 'green',
      duration: '2 kun oldin',
      price: 250000,
      rating: 4.8,
      students: 156,
      image: 'https://picsum.photos/seed/webdev/400/250.jpg'
    },
    {
      id: 2,
      title: 'Mobile Development',
      description: 'iOS va Android uchun ilovalar',
      mentor: 'Jane Smith',
      mentorImage: 'https://picsum.photos/seed/mentor2/32/32.jpg',
      category: 'Mashhur',
      categoryColor: 'blue',
      duration: '5 kun oldin',
      price: 350000,
      rating: 4.9,
      students: 234,
      image: 'https://picsum.photos/seed/mobiledev/400/250.jpg'
    },
    {
      id: 3,
      title: 'Data Science',
      description: 'Ma\'lumotlar tahlili va machine learning',
      mentor: 'Mike Johnson',
      mentorImage: 'https://picsum.photos/seed/mentor3/32/32.jpg',
      category: 'Premium',
      categoryColor: 'purple',
      duration: '1 hafta oldin',
      price: 450000,
      rating: 4.7,
      students: 89,
      image: 'https://picsum.photos/seed/datascience/400/250.jpg'
    },
    {
      id: 4,
      title: 'UI/UX Design',
      description: 'Foydalanuvchi interfeysi dizayni',
      mentor: 'Sarah Wilson',
      mentorImage: 'https://picsum.photos/seed/mentor4/32/32.jpg',
      category: 'Yangi',
      categoryColor: 'green',
      duration: '3 kun oldin',
      price: 200000,
      rating: 4.6,
      students: 112,
      image: 'https://picsum.photos/seed/uiux/400/250.jpg'
    },
    {
      id: 5,
      title: 'DevOps Engineering',
      description: 'Cloud va CI/CD asoslari',
      mentor: 'Tom Brown',
      mentorImage: 'https://picsum.photos/seed/mentor5/32/32.jpg',
      category: 'Advanced',
      categoryColor: 'orange',
      duration: '1 kun oldin',
      price: 550000,
      rating: 4.9,
      students: 67,
      image: 'https://picsum.photos/seed/devops/400/250.jpg'
    }
  ,
    {
      id: 5,
      title: 'DevOps Engineering',
      description: 'Cloud va CI/CD asoslari',
      mentor: 'Tom Brown',
      mentorImage: 'https://picsum.photos/seed/mentor5/32/32.jpg',
      category: 'Advanced',
      categoryColor: 'orange',
      duration: '1 kun oldin',
      price: 550000,
      rating: 4.9,
      students: 67,
      image: 'https://picsum.photos/seed/devops/400/250.jpg'
    }
  ,
    {
      id: 5,
      title: 'DevOps Engineering',
      description: 'Cloud va CI/CD asoslari',
      mentor: 'Tom Brown',
      mentorImage: 'https://picsum.photos/seed/mentor5/32/32.jpg',
      category: 'Advanced',
      categoryColor: 'orange',
      duration: '1 kun oldin',
      price: 550000,
      rating: 4.9,
      students: 67,
      image: 'https://picsum.photos/seed/devops/400/250.jpg'
    }
  ,
    {
      id: 5,
      title: 'DevOps Engineering',
      description: 'Cloud va CI/CD asoslari',
      mentor: 'Tom Brown',
      mentorImage: 'https://picsum.photos/seed/mentor5/32/32.jpg',
      category: 'Advanced',
      categoryColor: 'orange',
      duration: '1 kun oldin',
      price: 550000,
      rating: 4.9,
      students: 67,
      image: 'https://picsum.photos/seed/devops/400/250.jpg'
    }
  ,
    {
      id: 5,
      title: 'DevOps Engineering',
      description: 'Cloud va CI/CD asoslari',
      mentor: 'Tom Brown',
      mentorImage: 'https://picsum.photos/seed/mentor5/32/32.jpg',
      category: 'Advanced',
      categoryColor: 'orange',
      duration: '1 kun oldin',
      price: 550000,
      rating: 4.9,
      students: 67,
      image: 'https://picsum.photos/seed/devops/400/250.jpg'
    }
  ,
    {
      id: 5,
      title: 'DevOps Engineering',
      description: 'Cloud va CI/CD asoslari',
      mentor: 'Tom Brown',
      mentorImage: 'https://picsum.photos/seed/mentor5/32/32.jpg',
      category: 'Advanced',
      categoryColor: 'orange',
      duration: '1 kun oldin',
      price: 550000,
      rating: 4.9,
      students: 67,
      image: 'https://picsum.photos/seed/devops/400/250.jpg'
    }
  ,
    {
      id: 5,
      title: 'DevOps Engineering',
      description: 'Cloud va CI/CD asoslari',
      mentor: 'Tom Brown',
      mentorImage: 'https://picsum.photos/seed/mentor5/32/32.jpg',
      category: 'Advanced',
      categoryColor: 'orange',
      duration: '1 kun oldin',
      price: 550000,
      rating: 4.9,
      students: 67,
      image: 'https://picsum.photos/seed/devops/400/250.jpg'
    }
  ];

  ngOnInit(): void {
    this.startAutoSlide();
  }

  ngOnDestroy(): void {
    this.stopAutoSlide();
  }

  startAutoSlide(): void {
    this.autoSlideInterval = setInterval(() => {
      this.nextSlide();
    }, 5000); // 5 sekund
  }

  stopAutoSlide(): void {
    if (this.autoSlideInterval) {
      clearInterval(this.autoSlideInterval);
    }
  }

  nextSlide(): void {
    const maxIndex = Math.max(0, this.courses.length - 6); // 6 ta kurs ko'rinadi
    this.currentSlideIndex = (this.currentSlideIndex + 1) % (this.courses.length);

    // Agar oxirgi 6 taga yetib ketsa, boshidan boshlash
    if (this.currentSlideIndex > maxIndex) {
      this.currentSlideIndex = 0;
    }
  }

  prevSlide(): void {
    const maxIndex = Math.max(0, this.courses.length - 6);
    this.currentSlideIndex = this.currentSlideIndex - 1;

    // Agar 0 dan pastga tushsa, oxiridan boshlash
    if (this.currentSlideIndex < 0) {
      this.currentSlideIndex = maxIndex;
    }
  }

  goToSlide(index: number): void {
    const maxIndex = Math.max(0, this.courses.length - 6);

    // Faqat ruxsat etilgan indekslarga o'tish
    if (index <= maxIndex) {
      this.currentSlideIndex = index;
    }

    // Auto slide'ni qayta boshlash
    this.stopAutoSlide();
    this.startAutoSlide();
  }

  protected readonly Math = Math;
}
