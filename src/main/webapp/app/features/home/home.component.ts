import {Component, OnInit, OnDestroy} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LucideAngularModule} from "lucide-angular";
import {PageWrapperComponent} from "../../shared/ui";

@Component({
  selector: 'app-home',
  imports: [CommonModule, LucideAngularModule, PageWrapperComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  currentSlideIndex = 0;
  private autoSlideInterval: any;
  private touchStartX = 0;

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

  // Ko'rinadigan kartalar sonini aniqlash
  getVisibleCount(): number {
    if (typeof window === 'undefined') return 6;
    const width = window.innerWidth;
    if (width < 640) return 1.2;  // Mobilda keyingi karta biroz ko'rinib tursin (UX)
    if (width < 768) return 2;
    if (width < 1024) return 3;
    if (width < 1280) return 4;
    return 6;
  }

  nextSlide(): void {
    const visibleCount = Math.floor(this.getVisibleCount());
    const maxIndex = this.courses.length - visibleCount;

    if (this.currentSlideIndex >= maxIndex) {
      this.currentSlideIndex = 0; // Boshiga qaytish
    } else {
      this.currentSlideIndex++;
    }
  }

  prevSlide(): void {
    const visibleCount = Math.floor(this.getVisibleCount());
    const maxIndex = this.courses.length - visibleCount;

    if (this.currentSlideIndex <= 0) {
      this.currentSlideIndex = maxIndex; // Oxiriga o'tish
    } else {
      this.currentSlideIndex--;
    }
  }

  // Touch Eventlar
  onTouchStart(event: TouchEvent): void {
    this.touchStartX = event.touches[0].clientX;
    this.stopAutoSlide();
  }

  onTouchEnd(event: TouchEvent): void {
    const touchEndX = event.changedTouches[0].clientX;
    const diff = this.touchStartX - touchEndX;

    if (Math.abs(diff) > 50) { // 50px dan ortiq surilsa
      if (diff > 0) this.nextSlide();
      else this.prevSlide();
    }
    this.startAutoSlide();
  }

  startAutoSlide(): void {
    this.autoSlideInterval = setInterval(() => this.nextSlide(), 5000);
  }

  stopAutoSlide(): void {
    if (this.autoSlideInterval) clearInterval(this.autoSlideInterval);
  }
}

// Kurslar ro'yxati
