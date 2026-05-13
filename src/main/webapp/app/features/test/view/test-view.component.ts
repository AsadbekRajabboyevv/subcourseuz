import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TestService } from '../test.service';
import { AuthService } from '../../../common/auth/auth.service';
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";
import {MarkdownComponent} from "ngx-markdown";

@Component({
  selector: 'app-test-view',
  standalone: true,
  imports: [CommonModule, RouterLink, PageWrapperComponent, MarkdownComponent],
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
    const idParam = this.route.snapshot.paramMap.get('id');
    this.testId = idParam ? Number(idParam) : null;

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
      error: (err) => {
        console.error('Ma\'lumot yuklashda xatolik:', err);
        this.isLoading.set(false);
      }
    });
  }

  startTest(testId: number) {
    this.testService.start(testId).subscribe(res => {
      const sessionId = res.data;
      localStorage.setItem('active_session_id', sessionId.toString());
      this.router.navigate(['/test-process', sessionId]);
    });
  }
}
