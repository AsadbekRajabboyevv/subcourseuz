import { Pipe, PipeTransform, inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
  name: 'translate',
  standalone: true
})
export class TranslatePipe implements PipeTransform {
  private readonly translateService = inject(TranslateService);

  transform(key: string, params?: Record<string, any>): string {
    return this.translateService.instant(key, params);
  }
}
