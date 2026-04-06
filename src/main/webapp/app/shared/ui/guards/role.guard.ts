import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../../common/auth/auth.service';

export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/auth/login']);
    return false;
  }

  const requiredRoles: string[] = route.data['roles'] ?? [];
  const currentRole = authService.currentRole();

  if (requiredRoles.length > 0 && (!currentRole || !requiredRoles.includes(currentRole))) {
    router.navigate(['/unauthorized']);
    return false;
  }

  return true;
};
