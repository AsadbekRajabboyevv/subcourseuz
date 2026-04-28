import { Component, inject, OnInit, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import {PageWrapperComponent} from "../../../shared/ui/layout/page-wrapper.component";
import {ContainerComponent} from "../../../shared/ui/layout/container.component";
import {TopUpRequestService} from "../top-up.service";
import {TopUpStatus} from "../top-up.model";
@Component({
  selector: 'app-top-up-history',
  standalone: true,
  imports: [CommonModule, PageWrapperComponent, ContainerComponent],
  templateUrl: './top-up-list.component.html'
})
export class TopUpHistoryComponent implements OnInit {
  private readonly topUpService = inject(TopUpRequestService);
  requests = this.topUpService.requests;
  totalLength = this.topUpService.totalLength;
  currentPage = signal<number>(0);
  pageSize = 8;

  ngOnInit() {
    this.loadMyHistories();
  }

  loadMyHistories() {
    this.topUpService.getMy({}, this.currentPage(), this.pageSize).subscribe();
  }

  onCancel(id: number) {
    if (confirm('Ushbu to\'lov so\'rovini bekor qilmoqchimisiz?')) {
      this.topUpService.cancel(id).subscribe(() => {
        this.loadMyHistories();
      });
    }
  }

  changePage(delta: number) {
    const next = this.currentPage() + delta;
    if (next >= 0 && next * this.pageSize < this.totalLength()) {
      this.currentPage.set(next);
      this.loadMyHistories();
    }
  }

  getStatusClass(status: TopUpStatus): string {
    const base = 'px-3 py-1.5 rounded-full text-[10px] font-black uppercase tracking-tighter ';
    switch (status) {
      case TopUpStatus.APPROVED: return base + 'bg-emerald-500/10 text-emerald-600';
      case TopUpStatus.REJECTED: return base + 'bg-red-500/10 text-red-600';
      case TopUpStatus.CANCELLED: return base + 'bg-gray-100 text-gray-500';
      default: return base + 'bg-amber-500/10 text-amber-600 animate-pulse';
    }
  }
}
