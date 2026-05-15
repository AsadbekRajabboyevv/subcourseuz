import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { AuthLayoutComponent } from "./common/auth/auth.layout.component";
import {PrivacyPolicyComponent} from "./common/user-terms/privacy-policy.component";
import {TermsOfServiceComponent} from "./common/user-terms/terms-of-service.component";
import {OAuth2SuccessComponent} from "./common/auth/component/oauth2-succes.component";

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
    path: 'oauth2/success',
    component: OAuth2SuccessComponent
  },
  {
    path: 'tests-create',
    loadComponent: () => import('./features/test/create/test-create.component').then(m => m.TestCreateComponent),
    title: $localize`:@@test.title:Testlar yaratish`
  },
  {
    path: 'tests-update/:id',
    loadComponent: () => import('./features/test/update/test-update.component').then(m => m.TestUpdateComponent),
    title: $localize`:@@test.update.title:Testni yangilash`
  },
  {
    path: 'test-process/:id',
    loadComponent: () => import('./features/test/process/test-process.component').then(m => m.TestProcessComponent),
    title: $localize`:@@test.update.title:Test ishlash`
  },
  {
    path: 'test-result/:sessionId',
    loadComponent: () => import('./features/test/result/test-result.component').then(m => m.TestResultComponent),
    title: $localize`:@@test.update.title:Test natijasi`
  },
  {
    path: 'test-review-list',
    loadComponent: () => import('./features/test/review/test-review-list.component').then(m => m.TestReviewListComponent),
    title: $localize`:@@test.update.title:Testlar natijalari`
  },
  {
    path: 'test-review/:sessionId',
    loadComponent: () => import('./features/test/review/test-review.component').then(m => m.TestReviewComponent),
    title: $localize`:@@test.update.title:Testni ko'rib chiqish`
  },
  {
    path: 'tests-view/:id',
    loadComponent: () => import('./features/test/view/test-view.component').then(m => m.TestViewComponent),
    title: $localize`:@@test.update.title:Testni ishlash`
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
    path: 'tests-list',
    loadComponent: () => import('./features/test/list/test-list.component').then(m => m.TestListComponent),
    title: $localize`:@@test.list:Testlar ro‘yxati`
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
    path: 'courses-lesson-edit/:courseSlug',
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
    path: 'privacy-policy',
    component: PrivacyPolicyComponent,
    title: $localize`:@@privacy_policy_title:Maxfiylik siyosati`
  },
  {
    path: 'terms-of-service',
    component: TermsOfServiceComponent,
    title: $localize`:@@terms_of_service_title:Foydalanish shartlari`
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
