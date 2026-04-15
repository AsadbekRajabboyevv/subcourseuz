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
  TitleStrategy
} from '@angular/router';
import { provideHttpClient, withInterceptors} from '@angular/common/http';
import { routes } from 'app/app.routes';
import { CustomTitleStrategy } from 'app/common/title-strategy.injectable';
import { authInterceptor } from './common/auth/auth.interceptor';
import { errorInterceptor } from "./common/error/error.interceptor";

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

    {
      provide: TitleStrategy,
      useClass: CustomTitleStrategy
    }
  ]
};
