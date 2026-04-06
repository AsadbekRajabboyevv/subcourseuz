import { Directive, Input, TemplateRef, ViewContainerRef, effect, inject } from '@angular/core';
import { AuthService } from '../../../common/auth/auth.service';

@Directive({
  selector: '[appHasRole]',
  standalone: true,
})
export class HasRoleDirective {
  @Input('appHasRole') roles: string[] = [];

  private readonly authService = inject(AuthService);
  private readonly viewContainer = inject(ViewContainerRef);
  private readonly templateRef = inject(TemplateRef<unknown>);

  constructor() {
    effect(() => {
      const currentRole = this.authService.currentRole();
      if (currentRole && this.roles.includes(currentRole)) {
        if (this.viewContainer.length === 0) {
          this.viewContainer.createEmbeddedView(this.templateRef);
        }
      } else {
        this.viewContainer.clear();
      }
    });
  }
}
