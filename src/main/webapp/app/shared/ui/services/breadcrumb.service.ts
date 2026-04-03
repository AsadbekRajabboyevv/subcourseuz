import { Injectable, signal, inject } from '@angular/core';
import { ActivatedRouteSnapshot, NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';

export interface Breadcrumb {
  label: string;
  url: string;
}

@Injectable({ providedIn: 'root' })
export class BreadcrumbService {
  private router = inject(Router);

  breadcrumbs = signal<Breadcrumb[]>([]);

  constructor() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      const root = this.router.routerState.snapshot.root;
      const breadcrumbs: Breadcrumb[] = [];
      this.generateBreadcrumbs(root, [], breadcrumbs);
      this.breadcrumbs.set(breadcrumbs);
    });
  }

  private generateBreadcrumbs(route: ActivatedRouteSnapshot, parentUrl: string[], breadcrumbs: Breadcrumb[]) {
    if (route) {
      const routeUrl = parentUrl.concat(route.url.map(url => url.path));

      if (route.data['breadcrumb']) {
        breadcrumbs.push({
          label: route.data['breadcrumb'],
          url: '/' + routeUrl.join('/')
        });
      }

      if (route.firstChild) {
        this.generateBreadcrumbs(route.firstChild, routeUrl, breadcrumbs);
      }
    }
  }

  updateLabel(label: string) {
    this.breadcrumbs.update(items => {
      if (items.length > 0) {
        const last = items[items.length - 1];
        items[items.length - 1] = { ...last, label };
      }
      return [...items];
    });
  }
}
