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
      {
        path: 'confirm-success',
        loadComponent: () => import('./features/confirm/email-confirm.component').then(m => m.EmailConfirmComponent),
        title: 'Email tasdiqlash'
      },
      {
        path: 'confirm-error',
        loadComponent: () => import('./features/confirm/email-confirm.component').then(m => m.EmailConfirmComponent),
        title: 'Email tasdiqlash'
      }
    ]
  },
  {
    path: 'top-up-requests',
    loadComponent: () => import('./features/admin/topuprequest/list/top-up-request-list.component').then(m => m.TopUpRequestListComponent),
    title: 'So`rovlar ro`yxati'
  },
  {
    path: 'top-up',
    loadComponent: () => import('./features/topup/create/top-up-create.component').then(m => m.TopUpCreateComponent),
    title: 'Balansini to\'ldirish '
  },
  {
    path: 'top-up-histories',
    loadComponent: () => import('./features/topup/list/top-up-list.component').then(m => m.TopUpHistoryComponent),
    title: 'Balansini to\'ldirish so`rovlar tarixi'
  },
  {
    path: 'courses-list',
    loadComponent: () => import('./features/course/list/course-list.component').then(m => m.CourseListComponent),
    title: 'Kurslar ro\'yxati'
  },
  {
    path: 'courses-lesson-add/:courseName',
    loadComponent: () => import('./features/lesson/create/lesson-create.component').then(m => m.LessonCreateComponent),
    title: 'Dars qo\'shish'
  },
  {
    path: 'courses-lesson-edit/:lessonName',
    loadComponent: () => import('./features/lesson/update/lesson-update.component').then(m => m.LessonUpdateComponent),
    title: 'Dars tahrirlash'
  },
  {
    path: 'courses-view/:courseName',
    loadComponent: () => import('./features/course/view/course-view.component').then(m => m.CourseViewComponent),
    title: 'Kursni ko\'rish'
  },
  {
    path: 'courses-update/:courseName',
    loadComponent: () => import('./features/course/update/course-update.component').then(m => m.CourseUpdateComponent),
    title: 'Kursni tahrirlash'
  },
  {
    path: 'courses-create',
    loadComponent: () => import('./features/course/create/course-create.component').then(m => m.CourseCreateComponent),
    title: 'Kurs yaratish'
  },
  {
    path: 'courses-me',
    loadComponent: () => import('./features/course/me/course-me.component').then(m => m.CourseMeComponent),
    title: 'Mening kurslarim'
  },
  {
    path: 'course/view/:courseName/lesson/view/:lessonName',
    loadComponent: () => import('./features/lesson/view/lesson-view.component').then(m => m.LessonViewComponent),
    title: 'Dars'
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
