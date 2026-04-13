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
        loadComponent: ()=> import('./common/auth/component/login.component').then(m => m.LoginComponent),
        title: 'Kirish'
      },
      {
        path: 'register',
        loadComponent: ()=> import('./common/auth/component/register.component').then(m => m.RegisterComponent),
        title: 'Registratsiya'
      },
    ]
  },
  {
    path: 'courses-list',
    loadComponent: () => import('./features/course/list/course-list.component').then(m => m.CourseListComponent),
    title: 'Kurslar ro\'yxati'
  },
  // {
  //   path: 'course-update/:courseName',
  //   loadComponent: () => import('./features/course/list/course-list.component').then(m => m.CourseListComponent),
  //   title: 'Kursni tahrirlash'
  // },
  // {
  //   path: 'course-view/:courseName',
  //   loadComponent: () => import('./features/course/list/course-list.component').then(m => m.CourseListComponent),
  //   title: 'Kursni ko\'rish'
  // },
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
    loadComponent: () => import('./features/error/error.component').then(m => m.ErrorPageComponent)
  }
];
