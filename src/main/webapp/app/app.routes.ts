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
  {
    path: 'courses-create',
    loadComponent: () => import('./features/course/create/course-create.component').then(m => m.CourseCreateComponent),
    title: 'Kurs yaratish'
  },
  {
    path: 'courses-view/:courseName',
    loadComponent: () => import('./features/course/view/course-view.component').then(m => m.CourseViewComponent),
    title: 'Kursni ko\'rish'
  },
  {
    path: 'courses-me',
    loadComponent: () => import('./features/course/me/course-me.component').then(m => m.CourseMeComponent),
    title: 'Mening kurslarim'
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
    loadComponent: () => import('./features/error/error.component').then(m => m.ErrorPageComponent)
  }
];
