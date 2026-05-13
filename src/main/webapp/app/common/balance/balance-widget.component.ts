import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import {UserBalance} from "./balance.model";
import {BalanceService} from "./balance.service";

@Component({
  selector: 'app-balance-widget',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './balance-widget.component.html'
})
export class BalanceWidgetComponent implements OnInit {
  private balanceService = inject(BalanceService);

  balance = signal<UserBalance | null>(null);

  ngOnInit() {
    this.loadBalance();
  }

  loadBalance() {
    this.balanceService.getMyBalance().subscribe({
      next: (res) => {
        if (res && res.data) {
          this.balance.set(res.data);
        }
      },
      error: (err) => {
        console.error('Balansni yuklashda xato:', err);
      }
    });
  }
}
