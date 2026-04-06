import { HttpClientModule } from '@angular/common/http';
import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, ExtraOptions, TitleStrategy } from '@angular/router';
import { routes } from 'app/app.routes';
import { CustomTitleStrategy } from 'app/common/title-strategy.injectable';
import { LucideAngularModule } from 'lucide-angular';
import * as allIcons from 'lucide-angular';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import {JwtInterceptor} from "./common/auth/jwt.interceptor";

const routeConfig: ExtraOptions = {
  onSameUrlNavigation: 'reload',
  scrollPositionRestoration: 'enabled'
};

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(
      RouterModule.forRoot(routes, routeConfig),
      BrowserAnimationsModule,
      HttpClientModule,
      LucideAngularModule.pick(allIcons as any)
    ),

    provideZoneChangeDetection({ eventCoalescing: true }),

    {
      provide: TitleStrategy,
      useClass: CustomTitleStrategy
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ]
};
