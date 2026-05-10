import { Component, OnInit, inject, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputComponent } from "../../../shared/ui/forms/input.component";
import { PageWrapperComponent } from "../../../shared/ui/layout/page-wrapper.component";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { PaymentService } from "../../payment/payment.service";
import { AuthService } from "../../../common/auth/auth.service";
import { TestService } from "../test.service";
import { TestFilter } from "../test.model";

@Component({
  selector: 'app-test-list',
  standalone: true,
  imports: [CommonModule, FormsModule, InputComponent, PageWrapperComponent, RouterLink],
  templateUrl: './test-list.component.html'
})
export class TestListComponent implements OnInit {
  public testService = inject(TestService);
  private paymentService = inject(PaymentService);
  private router = inject(Router);
  protected authService = inject(AuthService);
  private route = inject(ActivatedRoute);

  isFilterVisible = false;
  page = 0;
  size = 12;
  hasMore = true;
  loading = false;
  showPaymentModal = false;
  selectedItem: any = null;

  // 1. UI-dagi inputlar bog'lanadigan ob'ekt
  filter: TestFilter = {
    name: '',
    isPublished: true,
    lang: '',
  };

  // 2. Serverga yuboriladigan ob'ekt (faqat "Filtrlash" bosilganda yangilanadi)
  appliedFilter: TestFilter = { ...this.filter };

  languages = [
    { label: 'Barchasi', value: '' },
    { label: 'O‘zbekcha', value: 'UZ' },
    { label: 'Ruscha', value: 'RU' },
    { label: 'Inglizcha', value: 'EN' }
  ];

  ngOnInit() {
    this.loadData();
    this.route.queryParams.subscribe(params => {
      if (params['buyNow'] === 'true' && params['id']) {
        this.handleAutoOpenAfterLogin(Number(params['id']));
      }
    });
  }

  @HostListener('window:scroll')
  onScroll() {
    const pos = window.scrollY + window.innerHeight;
    const max = document.documentElement.scrollHeight;
    if (pos >= max - 300 && !this.loading && this.hasMore) {
      this.loadData();
    }
  }

  loadData() {
    if (this.loading || !this.hasMore) return;
    this.loading = true;

    // MUHIM: So'rov faqat appliedFilter bilan yuboriladi
    this.testService.get(this.page, this.size, this.appliedFilter).subscribe({
      next: (res) => {
        const data = res.data.content;
        this.page === 0
          ? this.testService.setTests(data)
          : this.testService.appendTests(data);

        this.hasMore = !res.data.last;
        this.page++;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  // "Filtrlash" tugmasi bosilganda
  applyFilters() {
    // UI-dagi filter qiymatlarini serverga yuboriladigan ob'ektga ko'chiramiz
    this.appliedFilter = JSON.parse(JSON.stringify(this.filter));

    this.page = 0;
    this.hasMore = true;
    this.testService.setTests([]);
    this.loadData();
  }


  resetFilters() {
    this.filter = {
      name: '',
      isPublished: this.filter.isPublished,
      lang: '',
    };
    this.applyFilters();
  }

  // Tab (Faol/Qoralama) bosilganda
  changeTab(published: boolean) {
    if (this.filter.isPublished === published) return;
    this.filter.isPublished = published;

    // Tab almashganda inputda nimadir yozilgan bo'lsa, o'sha bilan birga filtrlaymiz
    this.applyFilters();
  }

  onBuy(item: any) {
    if (!this.authService.isLoggedIn()) {
      const returnUrl = `${this.router.url.split('?')[0]}?id=${item.id}&buyNow=true`;
      void this.router.navigate(['/auth/login'], { queryParams: { returnUrl } });
      return;
    }
    this.selectedItem = item;
    this.showPaymentModal = true;
  }

  private handleAutoOpenAfterLogin(testId: number) {
    const interval = setInterval(() => {
      const test = this.testService.tests().find(t => t.id === testId);
      if (test) {
        this.onBuy(test);
        void this.router.navigate([], { queryParams: { buyNow: null, id: null }, queryParamsHandling: 'merge' });
        clearInterval(interval);
      }
    }, 200);
    setTimeout(() => clearInterval(interval), 4000);
  }
}
