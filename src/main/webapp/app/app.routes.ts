import {Routes} from '@angular/router';
import {HomeComponent} from './features/home/home.component';
import {AuthLayoutComponent} from "./common/auth/auth.layout.component";
import {CourseComponent} from "./features/course/component/course.component";
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
    path: 'course/create',
    loadComponent: ()=> import('./features/course/component/course.component').then(m => m.CourseComponent),
    title: 'Kurs yaratish',
    data: {
      roles: ['ROLE_ADMIN', 'ROLE_TEACHER']
    }
  },
  {
    path: 'course/grade/create',
    loadComponent: ()=> import('./features/course/grade/course-grade.component').then(m => m.CourseGradeComponent),
    title: 'Kurs darajasini yaratish',
    data: {
      roles: ['ROLE_ADMIN', 'ROLE_STUDENT']
    }
  },
  {
    path: 'course/science/create',
    loadComponent: ()=> import('./features/course/component/course.component').then(m => m.CourseComponent),
    title: 'Kurs yaratish',
    data: {
      roles: ['ROLE_ADMIN', 'ROLE_TEACHER']
    }
  },


  {
    path: 'courses',
    loadComponent: ()=> import('./features/course/component/course.component').then(m => m.CourseComponent),
    title: 'Kurslar'
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
