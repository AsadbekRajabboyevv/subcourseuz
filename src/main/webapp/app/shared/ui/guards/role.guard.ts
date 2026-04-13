import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../../common/auth/auth.service';

export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    return router.createUrlTree(['/auth/login']);
  }

  const requiredRoles: string[] = route.data['roles'] ?? [];
  const currentRole = authService.currentUser()?.role;

  if (requiredRoles.length && (!currentRole || !requiredRoles.includes(currentRole))) {
    return router.createUrlTree(['/unauthorized']);
  }

  return true;
};
