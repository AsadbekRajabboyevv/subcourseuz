import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TestService } from '../test.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { marked } from 'marked';
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";
import {TestReview} from "../test.model";

@Component({
  selector: 'app-test-review',
  standalone: true,
  imports: [CommonModule, PageWrapperComponent],
  templateUrl: './test-review.component.html'
})
export class TestReviewComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private testService = inject(TestService);
  private sanitizer = inject(DomSanitizer);

  sessionId = signal<number | null>(null);
  reviewData = signal<TestReview | null>(null);

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('sessionId'));
    if (id) {
      this.sessionId.set(id);
      this.loadReview();
    }
  }

  loadReview() {
    this.testService.getReview(this.sessionId()!).subscribe({
      next: (res) => this.reviewData.set(res.data),
      error: (err) => console.error(err)
    });
  }

  getMarkdownAsHtml(content: string): SafeHtml {
    const html = marked.parse(content || '') as string;
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
}
