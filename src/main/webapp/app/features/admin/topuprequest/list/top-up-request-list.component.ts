import {Component, inject, OnInit, signal} from "@angular/core";
import {TopUpRequestService} from "../../../topup/top-up.service";
import {
  TopUpRequest,
  TopUpRequestAction,
  TopUpRequestFilter,
  TopUpStatus
} from "../../../topup/top-up.model";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {InputComponent} from "../../../../shared/ui/forms/input.component";
import {PageWrapperComponent} from "../../../../shared/ui/layout/page-wrapper.component";
import {ContainerComponent} from "../../../../shared/ui/layout/container.component";
import {GridComponent} from "../../../../shared/ui/layout/grid.component";
import {environment} from "../../../../../environments/environment";

@Component({
  selector: 'app-admin-top-up-request-list',
  standalone: true,
  imports: [CommonModule, FormsModule, InputComponent, PageWrapperComponent, ContainerComponent, GridComponent],
  templateUrl: './top-up-request-list.component.html'
})
export class TopUpRequestListComponent implements OnInit {
  private readonly topUpService = inject(TopUpRequestService);
  protected readonly environment = environment;
  requests = this.topUpService.requests;
  totalLength = this.topUpService.totalLength;

  selectedItem = signal<TopUpRequest | null>(null);
  currentPage = signal<number>(0);
  pageSize = 10;

  filterDto: TopUpRequestFilter = {
    transactionId: '',
    message: '',
    status: undefined,
    amountFrom: undefined,
    amountTo: undefined
  };

  actionDto: TopUpRequestAction = { id: 0, message: '', paymentExId: '' };
  statusOptions = Object.values(TopUpStatus).map(s => ({ label: s, value: s }));

  ngOnInit() {
    this.loadRequests();
  }

  loadRequests() {
    const cleanFilters = Object.fromEntries(
      Object.entries(this.filterDto).filter(([_, v]) => v !== null && v !== undefined && v !== '')
    );

    this.topUpService.getAll(cleanFilters, this.currentPage(), this.pageSize).subscribe();
  }

  applyFilter() {
    this.currentPage.set(0);
    this.loadRequests();
  }

  resetFilter() {
    this.filterDto = { transactionId: '', message: '', status: undefined, amountFrom: undefined, amountTo: undefined };
    this.applyFilter();
  }

  changePage(delta: number) {
    const next = this.currentPage() + delta;
    if (next >= 0 && next * this.pageSize < this.totalLength()) {
      this.currentPage.set(next);
      this.loadRequests();
    }
  }

  selectRequest(req: TopUpRequest) {
    this.selectedItem.set(req);
    this.actionDto = {
      id: req.id,
      message: '',
      paymentExId: req.paymentExId || ''
    };
  }

  onAccept() {
    if (!this.actionDto.paymentExId) {
      alert('Iltimos, Payment External ID ni kiriting!');
      return;
    }
    this.topUpService.accept(this.actionDto).subscribe(() => {
      this.selectedItem.set(null);
      this.loadRequests();
    });
  }

  onReject() {
    if (!this.actionDto.message) {
      alert('Iltimos, rad etish sababini yozing!');
      return;
    }
    this.topUpService.reject(this.actionDto).subscribe(() => {
      this.selectedItem.set(null);
      this.loadRequests();
    });
  }

  getStatusClass(status: TopUpStatus | string): string {
    const base = 'px-3 py-1 rounded-lg text-[9px] font-black uppercase tracking-wider border ';
    switch (status) {
      case TopUpStatus.APPROVED:
        return base + 'bg-emerald-100 text-emerald-600 border-emerald-200 dark:bg-emerald-500/10 dark:text-emerald-400 dark:border-emerald-500/20';
      case TopUpStatus.REJECTED:
        return base + 'bg-red-100 text-red-600 border-red-200 dark:bg-red-500/10 dark:text-red-400 dark:border-red-500/20';
      case TopUpStatus.CREATED:
      case TopUpStatus.PENDING:
        return base + 'bg-amber-100 text-amber-600 border-amber-200 dark:bg-amber-500/10 dark:text-amber-400 dark:border-amber-500/20';
      default:
        return base + 'bg-gray-100 text-gray-500 border-gray-200 dark:bg-gray-800 dark:text-gray-400';
    }
  }
}
