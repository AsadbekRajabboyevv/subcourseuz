import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { AuthLayoutComponent } from "./common/auth/auth.layout.component";

export const routes: Routes = [
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
      {
        path: 'login',
        loadComponent: () => import('./common/auth/component/login.component').then(m => m.LoginComponent),
        title: $localize`:@@login_title:Tizimga kirish`
      },
      {
        path: 'register',
        loadComponent: () => import('./common/auth/component/register.component').then(m => m.RegisterComponent),
        title: $localize`:@@register_title:Ro‘yxatdan o‘tish`
      },
      {
        path: 'confirm-success',
        loadComponent: () => import('./features/confirm/email-confirm.component').then(m => m.EmailConfirmComponent),
        title: $localize`:@@confirm_success:Email tasdiqlandi`
      },
      {
        path: 'confirm-error',
        loadComponent: () => import('./features/confirm/email-confirm.component').then(m => m.EmailConfirmComponent),
        title: $localize`:@@confirm_error:Tasdiqlashda xatolik`
      }
    ]
  },

  {
    path: 'top-up-requests',
    loadComponent: () => import('./features/admin/topuprequest/list/top-up-request-list.component').then(m => m.TopUpRequestListComponent),
    title: $localize`:@@topup_requests:So‘rovlar ro‘yxati`
  },
  {
    path: 'top-up',
    loadComponent: () => import('./features/topup/create/top-up-create.component').then(m => m.TopUpCreateComponent),
    title: $localize`:@@topup:Balansni to‘ldirish`
  },
  {
    path: 'top-up-histories',
    loadComponent: () => import('./features/topup/list/top-up-list.component').then(m => m.TopUpHistoryComponent),
    title: $localize`:@@topup_history:Balans tarixi`
  },

  {
    path: 'courses-list',
    loadComponent: () => import('./features/course/list/course-list.component').then(m => m.CourseListComponent),
    title: $localize`:@@courses_list:Kurslar ro‘yxati`
  },
  {
    path: 'courses-create',
    loadComponent: () => import('./features/course/create/course-create.component').then(m => m.CourseCreateComponent),
    title: $localize`:@@course_create:Kurs yaratish`
  },
  {
    path: 'courses-me',
    loadComponent: () => import('./features/course/me/course-me.component').then(m => m.CourseMeComponent),
    title: $localize`:@@my_courses:Mening kurslarim`
  },
  {
    path: 'courses-view/:slug',
    loadComponent: () => import('./features/course/view/course-view.component').then(m => m.CourseViewComponent),
    title: $localize`:@@course_view:Kursni ko‘rish`
  },
  {
    path: 'courses-update/:slug',
    loadComponent: () => import('./features/course/update/course-update.component').then(m => m.CourseUpdateComponent),
    title: $localize`:@@course_update:Kursni tahrirlash`
  },

  {
    path: 'courses-lesson-add/:slug',
    loadComponent: () => import('./features/lesson/create/lesson-create.component').then(m => m.LessonCreateComponent),
    title: $localize`:@@lesson_add:Dars qo‘shish`
  },
  {
    path: 'courses-lesson-edit/:lessonSlug',
    loadComponent: () => import('./features/lesson/update/lesson-update.component').then(m => m.LessonUpdateComponent),
    title: $localize`:@@lesson_edit:Dars tahrirlash`
  },

  {
    path: 'lesson/:courseSlug/:lessonSlug',
    loadComponent: () => import('./features/lesson/view/lesson-view.component').then(m => m.LessonViewComponent),
    title: $localize`:@@lesson_view:Dars`
  },

  {
    path: 'home',
    component: HomeComponent,
    title: $localize`:@@home_title:Bosh sahifa`
  },

  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },

  {
    path: '**',
    loadComponent: () => import('./features/error/error.component').then(m => m.ErrorPageComponent),
    title: $localize`:@@not_found:Sahifa topilmadi`
  }
];
