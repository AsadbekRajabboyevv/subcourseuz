import {Routes} from '@angular/router';
import {HomeComponent} from './features/home/home.component';
import {AuthLayoutComponent} from "./common/auth/auth.layout.component";
export const routes: Routes = [
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
      {
        path: 'login',
        loadComponent: ()=> import('./common/auth/auth.component').then(m => m.AuthComponent),
        title: 'Kirish'
      },
      {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: 'home',
    component: HomeComponent,
    title: 'Asosiy sahifa'
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: '**',
    loadComponent: () => import('./features/error/error.component').then(m => m.ErrorComponent)
  }
];
