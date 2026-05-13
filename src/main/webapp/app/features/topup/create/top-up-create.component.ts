import {Component, inject} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {TopUpRequestService} from "../top-up.service";
import {Router} from "@angular/router";
import {TopUpBalance} from "../top-up.model";
import {InputComponent} from "../../../shared/ui/forms/input.component";

@Component({
  selector: 'app-top-up-create',
  standalone: true,
  imports: [CommonModule, FormsModule, InputComponent],
  templateUrl: './top-up-create.component.html'
})
export class TopUpCreateComponent {
  private readonly topUpService = inject(TopUpRequestService);
  private readonly router = inject(Router);

  requestDto: TopUpBalance = { amount: 0, message: null };
  selectedFile: File | null = null;

  onFileSelected(file: any) {
    if (file instanceof File) {
      this.selectedFile = file;
    }
  }

  submitRequest() {
    if (!this.selectedFile || this.requestDto.amount <= 0) return;

    this.topUpService.create(this.requestDto, this.selectedFile).subscribe({
      next: (res) => {
        this.router.navigate(['/top-up-histories']);
      },
      error: (err) => {
        alert('Xatolik yuz berdi. Qayta urinib ko\'ring.');
        console.error(err);
      }
    });
  }
  copyToClipboard(text: string) {
    navigator.clipboard.writeText(text).then(() => {
      alert('Karta raqami nusxalandi!');
    }).catch(err => {
      console.error('Nusxalashda xatolik:', err);
    });
  }
}
