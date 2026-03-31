import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, of } from 'rxjs';
import { MenuItem } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class MenuService {
  private readonly http = inject(HttpClient);

  getMenuItems(role: string): Observable<MenuItem[]> {
    return this.http
      .get<MenuItem[]>(`/v1/api/menu?role=${role}`)
      .pipe(catchError(() => of(this.getStaticItems(role))));
  }

  private getStaticItems(role: string): MenuItem[] {
    const adminItems: MenuItem[] = [
      {
        id: 'dashboard',
        label: 'Boshqaruv paneli',
        icon: 'grid',
        route: '/admin/dashboard',
        roles: ['ROLE_ADMIN'],
        source: 'static',
      },
      {
        id: 'users',
        label: 'Foydalanuvchilar',
        icon: 'users',
        route: '/admin/users',
        roles: ['ROLE_ADMIN'],
        source: 'static',
      },
      {
        id: 'courses',
        label: 'Kurslar',
        icon: 'book',
        route: '/admin/courses',
        roles: ['ROLE_ADMIN'],
        source: 'static',
        children: [
          { id: 'courses-list', label: 'Ro\'yxat', route: '/admin/courses', source: 'static' },
          { id: 'courses-new', label: 'Yangi kurs', route: '/admin/courses/new', source: 'static' },
        ],
      },
      {
        id: 'balance',
        label: 'Balans',
        icon: 'credit-card',
        route: '/admin/balance',
        roles: ['ROLE_ADMIN'],
        source: 'static',
      },
      {
        id: 'settings',
        label: 'Sozlamalar',
        icon: 'settings',
        route: '/admin/settings',
        roles: ['ROLE_ADMIN'],
        source: 'static',
      },
    ];

    const userItems: MenuItem[] = [
      {
        id: 'home',
        label: 'Bosh sahifa',
        icon: 'home',
        route: '/home',
        roles: ['ROLE_USER'],
        source: 'static',
      },
      {
        id: 'my-courses',
        label: 'Mening kurslarim',
        icon: 'book-open',
        route: '/my-courses',
        roles: ['ROLE_USER'],
        source: 'static',
      },
      {
        id: 'my-balance',
        label: 'Mening balansom',
        icon: 'wallet',
        route: '/my-balance',
        roles: ['ROLE_USER'],
        source: 'static',
      },
      {
        id: 'profile',
        label: 'Profil',
        icon: 'user',
        route: '/profile',
        roles: ['ROLE_USER'],
        source: 'static',
      },
    ];

    return role === 'ROLE_ADMIN' ? adminItems : userItems;
  }
}
