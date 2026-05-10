import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TestService } from '../test.service';
import { AuthService } from '../../../common/auth/auth.service';
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";

@Component({
  selector: 'app-test-view',
  standalone: true,
  imports: [CommonModule, RouterLink, PageWrapperComponent],
  templateUrl: './test-view.component.html'
})
export class TestViewComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private testService = inject(TestService);
  protected authService = inject(AuthService);

  testId: number | null = null;
  testInfo = signal<any>(null);
  isLoading = signal(false);
  isStarting = signal(false);

  ngOnInit() {
    this.testId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.testId) {
      this.loadInfo(this.testId);
    }
  }

  loadInfo(id: number) {
    this.isLoading.set(true);
    this.testService.getInfo(id).subscribe({
      next: (res) => {
        this.testInfo.set(res.data);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  startTest() {
    if (!this.testId || this.isStarting()) return;

    this.isStarting.set(true);
    this.testService.start(this.testId).subscribe({
      next: (res) => {
        // res.data ichida sessionId keladi (Base<number>)
        const sessionId = res.data;
        // Test topshirish sahifasiga sessionId bilan o'tamiz
        this.router.navigate(['/tests-process', sessionId]);
      },
      error: (err) => {
        this.isStarting.set(false);
        console.error('Testni boshlashda xatolik:', err);
      }
    });
  }
}
