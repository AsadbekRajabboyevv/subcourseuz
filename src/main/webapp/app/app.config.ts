import {
  ApplicationConfig,
  importProvidersFrom,
  provideZoneChangeDetection
} from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  provideRouter,
  withRouterConfig,
  withInMemoryScrolling,
} from '@angular/router';
import { provideHttpClient, withInterceptors} from '@angular/common/http';
import { routes } from 'app/app.routes';
import { authInterceptor } from './common/auth/auth.interceptor';
import { errorInterceptor } from "./common/error/error.interceptor";
import { provideMarkdown } from 'ngx-markdown';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(
      routes,
      withRouterConfig({
        onSameUrlNavigation: 'reload'
      }),
      withInMemoryScrolling({
        scrollPositionRestoration: 'enabled',
        anchorScrolling: 'enabled'
      })
    ),

    provideHttpClient(
      withInterceptors([
        authInterceptor,
        errorInterceptor
      ])
    ),

    provideZoneChangeDetection({ eventCoalescing: true }),

    importProvidersFrom(
      BrowserAnimationsModule,
    ),

    provideMarkdown(),
  ]
};
