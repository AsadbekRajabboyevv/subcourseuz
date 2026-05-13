import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TestService } from '../test.service';
import { TestResult } from '../test.model';
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";

@Component({
  selector: 'app-test-result',
  standalone: true,
  imports: [CommonModule, RouterLink, PageWrapperComponent],
  templateUrl: './test-result.component.html'
})
export class TestResultComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private testService = inject(TestService);

  sessionId: number | null = null;

  result = signal<TestResult>({
    score: 0,
    correctAnswers: 0,
    totalQuestions: 0,
    spentTime: '',
    startedAt: '',
    finishedAt: ''
  });

  isPassed = computed(() => this.result().score >= 60);

  ngOnInit() {
    this.sessionId = Number(this.route.snapshot.paramMap.get('sessionId'));
    const stateResult = window.history.state?.result as TestResult;

    if (stateResult) {
      this.result.set(stateResult);
    } else if (this.sessionId) {
      this.loadResultFromReview();
    }
  }

  loadResultFromReview() {
    if (!this.result().totalQuestions) {
    }
  }

  goToReview() {
    this.router.navigate(['/test-review', this.sessionId]);
  }
}
