import { Component, OnInit, OnDestroy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TestService } from '../test.service';
import { marked } from 'marked';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";
import { TestSession, TestSessionQuestion } from '../test.model';

@Component({
  selector: 'app-test-process',
  standalone: true,
  imports: [CommonModule, PageWrapperComponent],
  templateUrl: './test-process.component.html'
})
export class TestProcessComponent implements OnInit, OnDestroy {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private testService = inject(TestService);
  private sanitizer = inject(DomSanitizer);

  sessionId = signal<number | null>(null);
  sessionData = signal<TestSession | null>(null);
  currentQuestionIdx = signal(0);
  isLoading = signal(true);
  isSubmitting = signal(false);
  remainingTime = signal<number>(0);

  private timerInterval: any;

  answeredCount = computed(() => {
    const data = this.sessionData();
    return data ? data.questions.filter(q => q.answered).length : 0;
  });

  ngOnInit() {
    this.initializeSession();
  }

  private initializeSession() {
    const urlSessionId = Number(this.route.snapshot.paramMap.get('sessionId'));
    const storedSessionId = localStorage.getItem('active_session_id');

    if (urlSessionId) {
      this.sessionId.set(urlSessionId);
      localStorage.setItem('active_session_id', urlSessionId.toString());
      this.loadSessionData();
    } else if (storedSessionId) {
      const sId = Number(storedSessionId);
      this.sessionId.set(sId);
      this.router.navigate(['/test-process', sId], { replaceUrl: true });
      this.loadSessionData();
    } else {
      this.router.navigate(['/tests-list']);
    }
  }

  loadSessionData() {
    this.isLoading.set(true);
    this.testService.getSession(this.sessionId()!).subscribe({
      next: (res) => {
        this.sessionData.set(res.data);
        this.remainingTime.set(res.data.remainingSeconds);
        this.startTimer();
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Session yuklashda xatolik:', err);
        localStorage.removeItem('active_session_id');
        this.router.navigate(['/tests-list']);
      }
    });
  }

  private startTimer() {
    if (this.timerInterval) clearInterval(this.timerInterval);

    this.timerInterval = setInterval(() => {
      if (this.remainingTime() > 0) {
        this.remainingTime.update(t => t - 1);
      } else {
        this.autoFinish();
      }
    }, 1000);
  }

  private autoFinish() {
    clearInterval(this.timerInterval);
    alert("Vaqtingiz tugadi! Test avtomatik yakunlanadi.");
    this.finishTest();
  }

  selectOption(questionId: number, optionId: number) {
    if (this.isSubmitting()) return;

    this.isSubmitting.set(true);
    this.testService.submitAnswer({
      sessionId: this.sessionId()!,
      questionId: questionId,
      optionId: optionId
    }).subscribe({
      next: () => {
        this.sessionData.update(data => {
          if (!data) return null;
          const question = data.questions.find(q => q.id === questionId);
          if (question) {
            question.selectedOptionId = optionId;
            question.answered = true;
          }
          return { ...data };
        });
        this.isSubmitting.set(false);
      },
      error: () => {
        this.isSubmitting.set(false);
        alert("Javobni saqlashda xatolik yuz berdi!");
      }
    });
  }

  confirmFinish() {
    const total = this.sessionData()?.questions.length;
    const answered = this.answeredCount();

    if (confirm(`Siz ${total} tadan ${answered} tasiga javob berdingiz. Testni yakunlaysizmi?`)) {
      this.finishTest();
    }
  }

  finishTest() {
    if (this.timerInterval) clearInterval(this.timerInterval);
    this.isLoading.set(true);

    this.testService.finish(this.sessionId()!).subscribe({
      next: (res) => {
        localStorage.removeItem('active_session_id');
        this.router.navigate(['/test-result', this.sessionId()], {
          state: { result: res.data }
        });
      },
      error: () => {
        this.isLoading.set(false);
        alert("Testni yakunlashda xatolik yuz berdi!");
        this.startTimer();
      }
    });
  }

  nextQuestion() {
    if (this.currentQuestionIdx() < (this.sessionData()?.questions.length || 0) - 1) {
      this.currentQuestionIdx.update(i => i + 1);
    }
  }

  prevQuestion() {
    if (this.currentQuestionIdx() > 0) {
      this.currentQuestionIdx.update(i => i - 1);
    }
  }

  goToQuestion(index: number) {
    this.currentQuestionIdx.set(index);
  }

  formatTime(seconds: number): string {
    const h = Math.floor(seconds / 3600);
    const m = Math.floor((seconds % 3600) / 60);
    const s = seconds % 60;
    return `${h > 0 ? h + ':' : ''}${m < 10 ? '0' + m : m}:${s < 10 ? '0' + s : s}`;
  }

  getMarkdownAsHtml(content: string): SafeHtml {
    const html = marked.parse(content || '') as string;
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }

  ngOnDestroy() {
    if (this.timerInterval) clearInterval(this.timerInterval);
  }
}
